package com.parcial.app.Controlador;

import com.parcial.app.Modelo.Tarea;
import com.parcial.app.Servicio.TareaServicio;
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
 * TareaRestControlador — REST API para Tareas Técnicas.
 * Roles: Desarrollador Frontend (RF-FE-01), Desarrollador Backend (RF-BE-01),
 *        QA Engineer (RF-QA-01) y DevOps (RF-DO-01).
 * Base URL: /api/tareas
 *
 * Contraparte Web (Thymeleaf): TareaWebControlador  →  /tareas
 *
 * NOTA: Clase original renombrada de TareaControlador.
 */
@RestController
@RequestMapping("/api/tareas")
public class TareaRestControlador {

    @Autowired
    private TareaServicio tareaServicio;

    // ── GET /api/tareas/proyecto/{idProyecto} ──────────────────────────────
    @GetMapping("/proyecto/{idProyecto}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Tarea>> listarPorProyecto(@PathVariable String idProyecto) {
        return ResponseEntity.ok(tareaServicio.listarPorProyecto(idProyecto));
    }

    // ── GET /api/tareas/historia/{idHistoria} ──────────────────────────────
    @GetMapping("/historia/{idHistoria}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Tarea>> listarPorHistoria(@PathVariable String idHistoria) {
        return ResponseEntity.ok(tareaServicio.listarPorHistoria(idHistoria));
    }

    // ── GET /api/tareas/mis-tareas ─────────────────────────────────────────
    @GetMapping("/mis-tareas")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Tarea>> misTareas(Authentication auth) {
        return ResponseEntity.ok(tareaServicio.listarPorAsignado(auth.getName()));
    }

    // ── GET /api/tareas/{id} ───────────────────────────────────────────────
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        Optional<Tarea> opt = tareaServicio.buscarPorId(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Tarea no encontrada: " + id));
        }
        return ResponseEntity.ok(opt.get());
    }

    // ── POST /api/tareas ───────────────────────────────────────────────────
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER','SCRUM_MASTER')")
    public ResponseEntity<?> crear(@RequestBody Tarea tarea,
                                   Authentication auth,
                                   HttpServletRequest request) {
        if (tarea.getTitulo() == null || tarea.getTitulo().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El título de la tarea es obligatorio."));
        }
        if (tarea.getIdProyecto() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El campo 'idProyecto' es obligatorio."));
        }
        Tarea guardada = tareaServicio.crear(tarea, auth.getName(), request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    // ── PUT /api/tareas/{id} ───────────────────────────────────────────────
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> actualizar(@PathVariable String id,
                                        @RequestBody Tarea tareaActualizada,
                                        Authentication auth,
                                        HttpServletRequest request) {
        Optional<Tarea> opt = tareaServicio.buscarPorId(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Tarea no encontrada: " + id));
        }
        tareaActualizada.setId(id);
        tareaActualizada.setFechaCreacion(opt.get().getFechaCreacion());
        Tarea guardada = tareaServicio.crear(tareaActualizada,
                auth.getName(), request.getRemoteAddr());
        return ResponseEntity.ok(guardada);
    }

    // ── PATCH /api/tareas/{id}/estado ──────────────────────────────────────
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
        if (tareaServicio.buscarPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Tarea no encontrada: " + id));
        }
        Tarea actualizada = tareaServicio.cambiarEstado(
                id, nuevoEstado.toUpperCase(), auth.getName(), request.getRemoteAddr());
        return ResponseEntity.ok(actualizada);
    }

    // ── PATCH /api/tareas/{id}/horas ───────────────────────────────────────
    @PatchMapping("/{id}/horas")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> registrarHoras(@PathVariable String id,
                                            @RequestBody Map<String, Integer> body,
                                            Authentication auth,
                                            HttpServletRequest request) {
        Integer horas = body.get("horas");
        if (horas == null || horas <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Las horas deben ser un número positivo."));
        }
        if (tareaServicio.buscarPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Tarea no encontrada: " + id));
        }
        Tarea actualizada = tareaServicio.registrarHoras(
                id, horas, auth.getName(), request.getRemoteAddr());
        return ResponseEntity.ok(actualizada);
    }

    // ── PATCH /api/tareas/{id}/git ─────────────────────────────────────────
    @PatchMapping("/{id}/git")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> vincularGit(@PathVariable String id,
                                         @RequestBody Map<String, String> body,
                                         Authentication auth,
                                         HttpServletRequest request) {
        Optional<Tarea> opt = tareaServicio.buscarPorId(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Tarea no encontrada: " + id));
        }
        Tarea tarea = opt.get();
        if (body.containsKey("ramaGit"))        tarea.setRamaGit(body.get("ramaGit"));
        if (body.containsKey("urlPullRequest")) tarea.setUrlPullRequest(body.get("urlPullRequest"));
        Tarea guardada = tareaServicio.crear(tarea, auth.getName(), request.getRemoteAddr());
        return ResponseEntity.ok(guardada);
    }

    // ── DELETE /api/tareas/{id} ────────────────────────────────────────────
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER','SCRUM_MASTER')")
    public ResponseEntity<?> eliminar(@PathVariable String id,
                                      Authentication auth,
                                      HttpServletRequest request) {
        if (tareaServicio.buscarPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Tarea no encontrada: " + id));
        }
        tareaServicio.cambiarEstado(id, "ELIMINADA", auth.getName(), request.getRemoteAddr());
        return ResponseEntity.ok(Map.of("mensaje", "Tarea eliminada correctamente."));
    }
}
