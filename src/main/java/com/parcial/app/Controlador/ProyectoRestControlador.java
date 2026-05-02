package com.parcial.app.Controlador;

import com.parcial.app.Modelo.Proyecto;
import com.parcial.app.Servicio.ProyectoServicio;
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

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoRestControlador {

    @Autowired
    private ProyectoServicio proyectoServicio;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Proyecto>> listarTodos() {
        return ResponseEntity.ok(proyectoServicio.listarTodos());
    }

    @GetMapping("/activos")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Proyecto>> listarActivos() {
        return ResponseEntity.ok(proyectoServicio.listarActivos());
    }

    @GetMapping("/estadisticas")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> estadisticas() {
        return ResponseEntity.ok(Map.of(
                "totalActivos", proyectoServicio.contarActivos(),
                "total",        proyectoServicio.listarTodos().size()
        ));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        Optional<Proyecto> opt = proyectoServicio.buscarPorId(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Proyecto no encontrado: " + id));
        }
        return ResponseEntity.ok(opt.get());
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")  // ✅ CAMBIADO: cualquier usuario autenticado
    public ResponseEntity<?> crear(@RequestBody Proyecto proyecto,
                                   Authentication auth,
                                   HttpServletRequest request) {
        if (proyecto.getNombre() == null || proyecto.getNombre().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El nombre del proyecto es obligatorio."));
        }
        
        // ✅ MANEJO SEGURO: Si auth es null, usar "anonimo"
        String usuarioId = (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonimo";
        
        Proyecto guardado = proyectoServicio.crear(proyecto, usuarioId, request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> actualizar(@PathVariable String id,
                                        @RequestBody Proyecto proyectoActualizado,
                                        Authentication auth,
                                        HttpServletRequest request) {
        Optional<Proyecto> opt = proyectoServicio.buscarPorId(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Proyecto no encontrado: " + id));
        }
        proyectoActualizado.setId(id);
        proyectoActualizado.setFechaCreacion(opt.get().getFechaCreacion());
        
        String usuarioId = (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonimo";
        
        Proyecto guardado = proyectoServicio.actualizar(proyectoActualizado, usuarioId, request.getRemoteAddr());
        return ResponseEntity.ok(guardado);
    }

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
        Optional<Proyecto> opt = proyectoServicio.buscarPorId(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Proyecto no encontrado: " + id));
        }
        Proyecto proyecto = opt.get();
        proyecto.setEstado(nuevoEstado.toUpperCase());
        
        String usuarioId = (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonimo";
        
        Proyecto guardado = proyectoServicio.actualizar(proyecto, usuarioId, request.getRemoteAddr());
        return ResponseEntity.ok(guardado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminar(@PathVariable String id,
                                      Authentication auth,
                                      HttpServletRequest request) {
        if (proyectoServicio.buscarPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Proyecto no encontrado: " + id));
        }
        String usuarioId = (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonimo";
        proyectoServicio.eliminar(id, usuarioId, request.getRemoteAddr());
        return ResponseEntity.ok(Map.of("mensaje", "Proyecto eliminado correctamente."));
    }
}