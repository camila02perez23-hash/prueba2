package com.parcial.app.Controlador;

import com.parcial.app.Modelo.Sprint;
import com.parcial.app.Repositorio.SprintRepositorio;
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
 * SprintRestControlador — REST API para Sprints.
 * Rol principal: Project Manager / Scrum Master (RF-PM-02, RF-PM-07).
 * Base URL: /api/sprints
 *
 * Contraparte Web (Thymeleaf): SprintWebControlador  →  /sprints
 */
@RestController
@RequestMapping("/api/sprints")
public class SprintRestControlador {

    @Autowired
    private SprintRepositorio sprintRepositorio;

    @Autowired
    private AuditoriaServicio auditoriaServicio;

    // ── GET /api/sprints/proyecto/{idProyecto} ─────────────────────────────
    @GetMapping("/proyecto/{idProyecto}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Sprint>> listarPorProyecto(@PathVariable String idProyecto) {
        return ResponseEntity.ok(
                sprintRepositorio.findByIdProyectoOrderByNumeroAsc(idProyecto));
    }

    // ── GET /api/sprints/proyecto/{idProyecto}/activo ──────────────────────
    @GetMapping("/proyecto/{idProyecto}/activo")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> sprintActivo(@PathVariable String idProyecto) {
        Optional<Sprint> opt = sprintRepositorio.findByIdProyectoAndEstado(idProyecto, "EN_CURSO");
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "No hay sprint activo en este proyecto."));
        }
        return ResponseEntity.ok(opt.get());
    }

    // ── GET /api/sprints/{id} ──────────────────────────────────────────────
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        Optional<Sprint> opt = sprintRepositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Sprint no encontrado: " + id));
        }
        return ResponseEntity.ok(opt.get());
    }

    // ── POST /api/sprints ──────────────────────────────────────────────────
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER','SCRUM_MASTER')")
    public ResponseEntity<?> crear(@RequestBody Sprint sprint,
                                   Authentication auth,
                                   HttpServletRequest request) {
        if (sprint.getIdProyecto() == null || sprint.getIdProyecto().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El campo 'idProyecto' es obligatorio."));
        }
        if (sprint.getFechaInicio() == null || sprint.getFechaFin() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Las fechas de inicio y fin son obligatorias."));
        }
        if (sprint.getFechaFin().isBefore(sprint.getFechaInicio())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "La fecha de fin no puede ser anterior a la de inicio."));
        }
        sprint.setFechaCreacion(LocalDateTime.now());
        Sprint guardado = sprintRepositorio.save(sprint);
        auditoriaServicio.registrar(null, auth.getName(), "CREATE",
                "sprints", guardado.getId(),
                null, "Sprint #" + guardado.getNumero(), request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    // ── PUT /api/sprints/{id} ──────────────────────────────────────────────
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER','SCRUM_MASTER')")
    public ResponseEntity<?> actualizar(@PathVariable String id,
                                        @RequestBody Sprint sprintActualizado,
                                        Authentication auth,
                                        HttpServletRequest request) {
        Optional<Sprint> opt = sprintRepositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Sprint no encontrado: " + id));
        }
        Sprint existente = opt.get();
        sprintActualizado.setId(id);
        sprintActualizado.setFechaCreacion(existente.getFechaCreacion());
        Sprint guardado = sprintRepositorio.save(sprintActualizado);
        auditoriaServicio.registrar(null, auth.getName(), "UPDATE",
                "sprints", id, existente.getEstado(),
                guardado.getEstado(), request.getRemoteAddr());
        return ResponseEntity.ok(guardado);
    }

    // ── PATCH /api/sprints/{id}/iniciar ───────────────────────────────────
    @PatchMapping("/{id}/iniciar")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER','SCRUM_MASTER')")
    public ResponseEntity<?> iniciar(@PathVariable String id,
                                     Authentication auth,
                                     HttpServletRequest request) {
        Optional<Sprint> opt = sprintRepositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Sprint no encontrado: " + id));
        }
        Sprint sprint = opt.get();
        if (!"PLANIFICADO".equals(sprint.getEstado())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Solo se puede iniciar un sprint en estado PLANIFICADO."));
        }
        sprint.setEstado("EN_CURSO");
        Sprint guardado = sprintRepositorio.save(sprint);
        auditoriaServicio.registrar(null, auth.getName(), "INICIAR",
                "sprints", id, "PLANIFICADO", "EN_CURSO", request.getRemoteAddr());
        return ResponseEntity.ok(guardado);
    }

    // ── PATCH /api/sprints/{id}/completar ─────────────────────────────────
    @PatchMapping("/{id}/completar")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER','SCRUM_MASTER')")
    public ResponseEntity<?> completar(@PathVariable String id,
                                       Authentication auth,
                                       HttpServletRequest request) {
        Optional<Sprint> opt = sprintRepositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Sprint no encontrado: " + id));
        }
        Sprint sprint = opt.get();
        if (!"EN_CURSO".equals(sprint.getEstado())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Solo se puede completar un sprint EN_CURSO."));
        }
        sprint.setEstado("COMPLETADO");
        Sprint guardado = sprintRepositorio.save(sprint);
        auditoriaServicio.registrar(null, auth.getName(), "COMPLETAR",
                "sprints", id, "EN_CURSO", "COMPLETADO", request.getRemoteAddr());
        return ResponseEntity.ok(guardado);
    }

    // ── DELETE /api/sprints/{id} ───────────────────────────────────────────
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER','SCRUM_MASTER')")
    public ResponseEntity<?> eliminar(@PathVariable String id,
                                      Authentication auth,
                                      HttpServletRequest request) {
        Optional<Sprint> opt = sprintRepositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Sprint no encontrado: " + id));
        }
        if (!"PLANIFICADO".equals(opt.get().getEstado())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error",
                            "No se puede eliminar un sprint que ya fue iniciado o completado."));
        }
        auditoriaServicio.registrar(null, auth.getName(), "DELETE",
                "sprints", id, id, null, request.getRemoteAddr());
        sprintRepositorio.deleteById(id);
        return ResponseEntity.ok(Map.of("mensaje", "Sprint eliminado correctamente."));
    }
}
