package com.parcial.app.Controlador;

import com.parcial.app.Modelo.HistoriaUsuario;
import com.parcial.app.Repositorio.HistoriaUsuarioRepositorio;
import com.parcial.app.Servicio.AuditoriaServicio;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * HistoriaUsuarioRestControlador — REST API para Historias de Usuario.
 * Rol principal: Product Owner (RF-PO-01..07) y Analista de Negocio (RF-AN-06).
 * Ciclo de vida: PENDIENTE → EN_DESARROLLO → PARA_ACEPTACION → ACEPTADA | RECHAZADA
 * Base URL: /api/historias
 *
 * Contraparte Web (Thymeleaf): HistoriaUsuarioWebControlador  →  /historias
 *
 * NOTA: Clase original renombrada de HistoriaUsuarioControlado (typo corregido).
 */
@RestController
@RequestMapping("/api/historias")
public class HistoriaUsuarioRestControlador {

    @Autowired
    private HistoriaUsuarioRepositorio historiaRepositorio;

    @Autowired
    private AuditoriaServicio auditoriaServicio;

    // ── GET /api/historias/proyecto/{idProyecto} ───────────────────────────
    @GetMapping("/proyecto/{idProyecto}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<HistoriaUsuario>> listarPorProyecto(
            @PathVariable String idProyecto) {
        return ResponseEntity.ok(historiaRepositorio.findByIdProyecto(idProyecto));
    }

    // ── GET /api/historias/proyecto/{idProyecto}/backlog ───────────────────
    @GetMapping("/proyecto/{idProyecto}/backlog")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<HistoriaUsuario>> listarBacklogSinSprint(
            @PathVariable String idProyecto) {
        return ResponseEntity.ok(
                historiaRepositorio.findByIdProyectoAndIdSprintIsNull(idProyecto));
    }

    // ── GET /api/historias/sprint/{idSprint} ──────────────────────────────
    @GetMapping("/sprint/{idSprint}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<HistoriaUsuario>> listarPorSprint(
            @PathVariable String idSprint) {
        return ResponseEntity.ok(historiaRepositorio.findByIdSprint(idSprint));
    }

    // ── GET /api/historias/epica/{idEpica} ────────────────────────────────
    @GetMapping("/epica/{idEpica}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<HistoriaUsuario>> listarPorEpica(
            @PathVariable String idEpica) {
        return ResponseEntity.ok(historiaRepositorio.findByIdEpica(idEpica));
    }

    // ── GET /api/historias/proyecto/{idProyecto}/para-aceptacion ──────────
    @GetMapping("/proyecto/{idProyecto}/para-aceptacion")
    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER')")
    public ResponseEntity<List<HistoriaUsuario>> listarParaAceptacion(
            @PathVariable String idProyecto) {
        return ResponseEntity.ok(
                historiaRepositorio.findByIdProyectoAndEstado(idProyecto, "PARA_ACEPTACION"));
    }

    // ── GET /api/historias/{id} ────────────────────────────────────────────
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        Optional<HistoriaUsuario> opt = historiaRepositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Historia no encontrada: " + id));
        }
        return ResponseEntity.ok(opt.get());
    }

    // ── POST /api/historias ────────────────────────────────────────────────
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER','ANALISTA')")
    public ResponseEntity<?> crear(@RequestBody HistoriaUsuario historia,
                                   Authentication auth,
                                   HttpServletRequest request) {
        if (historia.getTitulo() == null || historia.getTitulo().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El título de la historia es obligatorio."));
        }
        if (historia.getIdProyecto() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El campo 'idProyecto' es obligatorio."));
        }
        historia.setFechaCreacion(LocalDateTime.now());
        HistoriaUsuario guardada = historiaRepositorio.save(historia);
        auditoriaServicio.registrar(null, auth.getName(), "CREATE",
                "historias_usuario", guardada.getId(),
                null, guardada.getTitulo(), request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    // ── PUT /api/historias/{id} ────────────────────────────────────────────
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER','ANALISTA')")
    public ResponseEntity<?> actualizar(@PathVariable String id,
                                        @RequestBody HistoriaUsuario historiaActualizada,
                                        Authentication auth,
                                        HttpServletRequest request) {
        Optional<HistoriaUsuario> opt = historiaRepositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Historia no encontrada: " + id));
        }
        historiaActualizada.setId(id);
        historiaActualizada.setFechaCreacion(opt.get().getFechaCreacion());
        HistoriaUsuario guardada = historiaRepositorio.save(historiaActualizada);
        auditoriaServicio.registrar(null, auth.getName(), "UPDATE",
                "historias_usuario", id,
                opt.get().getEstado(), guardada.getEstado(), request.getRemoteAddr());
        return ResponseEntity.ok(guardada);
    }

    // ── PATCH /api/historias/{id}/aprobar ─────────────────────────────────
    @PatchMapping("/{id}/aprobar")
    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER')")
    public ResponseEntity<?> aprobar(@PathVariable String id,
                                     @RequestBody(required = false) Map<String, String> body,
                                     Authentication auth,
                                     HttpServletRequest request) {
        Optional<HistoriaUsuario> opt = historiaRepositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Historia no encontrada: " + id));
        }
        HistoriaUsuario historia = opt.get();
        historia.setEstado("ACEPTADA");
        if (body != null && body.containsKey("observacion")) {
            historia.setObservacionAceptacion(body.get("observacion"));
        }
        HistoriaUsuario guardada = historiaRepositorio.save(historia);
        auditoriaServicio.registrar(null, auth.getName(), "APROBAR",
                "historias_usuario", id, "PARA_ACEPTACION", "ACEPTADA",
                request.getRemoteAddr());
        return ResponseEntity.ok(guardada);
    }

    // ── PATCH /api/historias/{id}/rechazar ────────────────────────────────
    @PatchMapping("/{id}/rechazar")
    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER')")
    public ResponseEntity<?> rechazar(@PathVariable String id,
                                      @RequestBody Map<String, String> body,
                                      Authentication auth,
                                      HttpServletRequest request) {
        String observacion = body.getOrDefault("observacion", "").trim();
        if (observacion.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Debe indicar el motivo del rechazo."));
        }
        Optional<HistoriaUsuario> opt = historiaRepositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Historia no encontrada: " + id));
        }
        HistoriaUsuario historia = opt.get();
        historia.setEstado("RECHAZADA");
        historia.setObservacionAceptacion(observacion);
        HistoriaUsuario guardada = historiaRepositorio.save(historia);
        auditoriaServicio.registrar(null, auth.getName(), "RECHAZAR",
                "historias_usuario", id, "PARA_ACEPTACION", "RECHAZADA",
                request.getRemoteAddr());
        return ResponseEntity.ok(guardada);
    }

    // ── PATCH /api/historias/{id}/asignar-sprint ──────────────────────────
    @PatchMapping("/{id}/asignar-sprint")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER','SCRUM_MASTER')")
    public ResponseEntity<?> asignarSprint(@PathVariable String id,
                                           @RequestBody Map<String, String> body,
                                           Authentication auth,
                                           HttpServletRequest request) {
        String idSprint = body.get("idSprint");
        if (idSprint == null || idSprint.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El campo 'idSprint' es obligatorio."));
        }
        Optional<HistoriaUsuario> opt = historiaRepositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Historia no encontrada: " + id));
        }
        HistoriaUsuario historia = opt.get();
        historia.setIdSprint(idSprint);
        HistoriaUsuario guardada = historiaRepositorio.save(historia);
        auditoriaServicio.registrar(null, auth.getName(), "ASIGNAR_SPRINT",
                "historias_usuario", id, null, idSprint, request.getRemoteAddr());
        return ResponseEntity.ok(guardada);
    }

    // ── POST /api/historias/{id}/comentarios ──────────────────────────────
    @PostMapping("/{id}/comentarios")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> agregarComentario(@PathVariable String id,
                                               @RequestBody Map<String, String> body,
                                               Authentication auth) {
        String texto = body.getOrDefault("texto", "").trim();
        if (texto.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El comentario no puede estar vacío."));
        }
        Optional<HistoriaUsuario> opt = historiaRepositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Historia no encontrada: " + id));
        }
        HistoriaUsuario historia = opt.get();
        historia.getComentarios().add("[" + auth.getName() + "] " + texto);
        return ResponseEntity.ok(historiaRepositorio.save(historia));
    }

    // ── DELETE /api/historias/{id} ────────────────────────────────────────
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER')")
    public ResponseEntity<?> eliminar(@PathVariable String id,
                                      Authentication auth,
                                      HttpServletRequest request) {
        if (historiaRepositorio.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Historia no encontrada: " + id));
        }
        auditoriaServicio.registrar(null, auth.getName(), "DELETE",
                "historias_usuario", id, id, null, request.getRemoteAddr());
        historiaRepositorio.deleteById(id);
        return ResponseEntity.ok(Map.of("mensaje", "Historia eliminada correctamente."));
    }
}
