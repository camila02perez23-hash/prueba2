package com.parcial.app.Controlador;

import com.parcial.app.Modelo.Defecto;
import com.parcial.app.Servicio.DefectoServicio;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * DefectoRestControlador — REST API para el módulo de Aseguramiento de Calidad.
 * Rol principal: QA Engineer (RF-QA-04, RF-QA-05) y Frontend (RF-FE-08).
 * Ciclo de vida: NUEVO → ASIGNADO → EN_DESARROLLO → RESUELTO → EN_VERIFICACION → CERRADO | REABIERTO
 * Base URL: /api/defectos
 *
 * Contraparte Web (Thymeleaf): DefectoWebControlador  →  /defectos
 *
 * NOTA: Clase original renombrada de DefectoControlador.
 */
@RestController
@RequestMapping("/api/defectos")
public class DefectoRestControlador {

    @Autowired
    private DefectoServicio defectoServicio;

    // ── GET /api/defectos/proyecto/{idProyecto} ────────────────────────────
    @GetMapping("/proyecto/{idProyecto}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Defecto>> listarPorProyecto(@PathVariable String idProyecto) {
        return ResponseEntity.ok(defectoServicio.listarPorProyecto(idProyecto));
    }

    // ── GET /api/defectos/asignados-a-mi ──────────────────────────────────
    @GetMapping("/asignados-a-mi")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Defecto>> misDefectos(Authentication auth) {
        return ResponseEntity.ok(defectoServicio.listarPorAsignado(auth.getName()));
    }

    // ── GET /api/defectos/proyecto/{idProyecto}/abiertos ──────────────────
    @GetMapping("/proyecto/{idProyecto}/abiertos")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Long>> contarAbiertos(@PathVariable String idProyecto) {
        return ResponseEntity.ok(
                Map.of("defectosAbiertos", defectoServicio.contarAbiertos(idProyecto)));
    }

    // ── GET /api/defectos/{id} ─────────────────────────────────────────────
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        Optional<Defecto> opt = defectoServicio.buscarPorId(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Defecto no encontrado: " + id));
        }
        return ResponseEntity.ok(opt.get());
    }

    // ── POST /api/defectos ─────────────────────────────────────────────────
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> reportar(@RequestBody Defecto defecto,
                                      Authentication auth,
                                      HttpServletRequest request) {
        if (defecto.getTitulo() == null || defecto.getTitulo().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El título del defecto es obligatorio."));
        }
        if (defecto.getIdProyecto() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El campo 'idProyecto' es obligatorio."));
        }
        defecto.setIdReportadoPor(auth.getName());
        Defecto guardado = defectoServicio.reportar(defecto,
                auth.getName(), request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    // ── PUT /api/defectos/{id} ─────────────────────────────────────────────
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','QA','FRONTEND','BACKEND')")
    public ResponseEntity<?> actualizar(@PathVariable String id,
                                        @RequestBody Defecto defectoActualizado,
                                        Authentication auth,
                                        HttpServletRequest request) {
        Optional<Defecto> opt = defectoServicio.buscarPorId(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Defecto no encontrado: " + id));
        }
        Defecto existente = opt.get();
        defectoActualizado.setId(id);
        defectoActualizado.setFechaReporte(existente.getFechaReporte());
        defectoActualizado.setIdReportadoPor(existente.getIdReportadoPor());
        Defecto guardado = defectoServicio.reportar(defectoActualizado,
                auth.getName(), request.getRemoteAddr());
        return ResponseEntity.ok(guardado);
    }

    // ── PATCH /api/defectos/{id}/asignar ──────────────────────────────────
    @PatchMapping("/{id}/asignar")
    @PreAuthorize("hasAnyRole('ADMIN','QA','PROJECT_MANAGER','SCRUM_MASTER')")
    public ResponseEntity<?> asignar(@PathVariable String id,
                                     @RequestBody Map<String, String> body,
                                     Authentication auth,
                                     HttpServletRequest request) {
        String idDesarrollador = body.get("idAsignadoA");
        if (idDesarrollador == null || idDesarrollador.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El campo 'idAsignadoA' es obligatorio."));
        }
        Optional<Defecto> opt = defectoServicio.buscarPorId(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Defecto no encontrado: " + id));
        }
        Defecto defecto = opt.get();
        defecto.setIdAsignadoA(idDesarrollador);
        defecto.setEstado("ASIGNADO");
        Defecto guardado = defectoServicio.reportar(defecto,
                auth.getName(), request.getRemoteAddr());
        return ResponseEntity.ok(guardado);
    }

    // ── PATCH /api/defectos/{id}/estado ───────────────────────────────────
    @PatchMapping("/{id}/estado")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cambiarEstado(@PathVariable String id,
                                           @RequestBody Map<String, String> body,
                                           Authentication auth,
                                           HttpServletRequest request) {
        String nuevoEstado = body.get("estado");
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El campo 'estado' es obligatorio."));
        }
        if (defectoServicio.buscarPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Defecto no encontrado: " + id));
        }
        Defecto actualizado = defectoServicio.cambiarEstado(
                id, nuevoEstado.toUpperCase(), auth.getName(), request.getRemoteAddr());
        return ResponseEntity.ok(actualizado);
    }

    // ── PATCH /api/defectos/{id}/cerrar ───────────────────────────────────
    @PatchMapping("/{id}/cerrar")
    @PreAuthorize("hasAnyRole('ADMIN','QA')")
    public ResponseEntity<?> cerrar(@PathVariable String id,
                                    Authentication auth,
                                    HttpServletRequest request) {
        if (defectoServicio.buscarPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Defecto no encontrado: " + id));
        }
        Defecto cerrado = defectoServicio.cambiarEstado(
                id, "CERRADO", auth.getName(), request.getRemoteAddr());
        return ResponseEntity.ok(cerrado);
    }

    // ── PATCH /api/defectos/{id}/reabrir ──────────────────────────────────
    @PatchMapping("/{id}/reabrir")
    @PreAuthorize("hasAnyRole('ADMIN','QA')")
    public ResponseEntity<?> reabrir(@PathVariable String id,
                                     Authentication auth,
                                     HttpServletRequest request) {
        if (defectoServicio.buscarPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Defecto no encontrado: " + id));
        }
        Defecto reabierto = defectoServicio.cambiarEstado(
                id, "REABIERTO", auth.getName(), request.getRemoteAddr());
        return ResponseEntity.ok(reabierto);
    }

    // ── DELETE /api/defectos/{id} ──────────────────────────────────────────
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminar(@PathVariable String id,
                                      Authentication auth,
                                      HttpServletRequest request) {
        if (defectoServicio.buscarPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Defecto no encontrado: " + id));
        }
        defectoServicio.cambiarEstado(
                id, "ELIMINADO", auth.getName(), request.getRemoteAddr());
        return ResponseEntity.ok(Map.of("mensaje", "Defecto eliminado correctamente."));
    }
}
