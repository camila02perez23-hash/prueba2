package com.parcial.app.Controlador;

import com.parcial.app.Modelo.Pipelinecicd;
import com.parcial.app.Repositorio.Pipelinecicdrepositorio;
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

/**
 * PipelineCicdRestControlador — REST API para Pipelines CI/CD.
 * Rol principal: DevOps Engineer (RF-DO-01, RF-DO-02, RF-DO-04).
 * Base URL: /api/pipelines
 *
 * Contraparte Web (Thymeleaf): PipelineCicdWebControlador  →  /pipelines
 *
 * NOTA: Clase original renombrada de PipelineCicdControlador.
 */
@RestController
@RequestMapping("/api/pipelines")
public class PipelineCicdRestControlador {

    @Autowired
    private Pipelinecicdrepositorio pipelineRepositorio;

    @Autowired
    private AuditoriaServicio auditoriaServicio;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DEVOPS')")
    public ResponseEntity<List<Pipelinecicd>> listarTodos() {
        return ResponseEntity.ok(pipelineRepositorio.findAll());
    }

    @GetMapping("/proyecto/{idProyecto}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Pipelinecicd>> listarPorProyecto(
            @PathVariable String idProyecto) {
        return ResponseEntity.ok(pipelineRepositorio.findByIdProyecto(idProyecto));
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('ADMIN','DEVOPS','PROJECT_MANAGER')")
    public ResponseEntity<List<Pipelinecicd>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(
                pipelineRepositorio.findByEstadoUltimaEjecucion(estado.toUpperCase()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        return pipelineRepositorio.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Pipeline no encontrado: " + id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DEVOPS')")
    public ResponseEntity<?> crear(@RequestBody Pipelinecicd pipeline,
                                   Authentication auth,
                                   HttpServletRequest request) {
        if (pipeline.getNombre() == null || pipeline.getNombre().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El nombre del pipeline es obligatorio."));
        }
        if (pipeline.getIdProyecto() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El campo 'idProyecto' es obligatorio."));
        }
        pipeline.setFechaCreacion(LocalDateTime.now());
        pipeline.setEstadoUltimaEjecucion("PENDIENTE");
        Pipelinecicd guardado = pipelineRepositorio.save(pipeline);
        auditoriaServicio.registrar(null, auth.getName(), "CREATE",
                "pipelines", guardado.getId(),
                null, guardado.getNombre(), request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DEVOPS')")
    public ResponseEntity<?> actualizar(@PathVariable String id,
                                        @RequestBody Pipelinecicd pipelineActualizado,
                                        Authentication auth,
                                        HttpServletRequest request) {
        return pipelineRepositorio.findById(id).<ResponseEntity<?>>map(existente -> {
            pipelineActualizado.setId(id);
            pipelineActualizado.setFechaCreacion(existente.getFechaCreacion());
            Pipelinecicd guardado = pipelineRepositorio.save(pipelineActualizado);
            auditoriaServicio.registrar(null, auth.getName(), "UPDATE",
                    "pipelines", id, existente.getNombre(),
                    guardado.getNombre(), request.getRemoteAddr());
            return ResponseEntity.ok(guardado);
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Pipeline no encontrado: " + id)));
    }

    @PatchMapping("/{id}/ejecutar")
    @PreAuthorize("hasAnyRole('ADMIN','DEVOPS')")
    public ResponseEntity<?> ejecutar(@PathVariable String id,
                                      Authentication auth,
                                      HttpServletRequest request) {
        return pipelineRepositorio.findById(id).map(pipeline -> {
            pipeline.setEstadoUltimaEjecucion("EN_EJECUCION");
            pipeline.setFechaUltimaEjecucion(LocalDateTime.now());
            Pipelinecicd guardado = pipelineRepositorio.save(pipeline);
            auditoriaServicio.registrar(null, auth.getName(), "EJECUTAR",
                    "pipelines", id, "PENDIENTE", "EN_EJECUCION", request.getRemoteAddr());
            return ResponseEntity.ok((Object) guardado);
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Pipeline no encontrado: " + id)));
    }

    @PatchMapping("/{id}/resultado")
    @PreAuthorize("hasAnyRole('ADMIN','DEVOPS')")
    public ResponseEntity<?> actualizarResultado(@PathVariable String id,
                                                 @RequestBody Map<String, String> body,
                                                 Authentication auth,
                                                 HttpServletRequest request) {
        String estado = body.get("estado");
        if (estado == null || (!estado.equalsIgnoreCase("EXITOSO")
                && !estado.equalsIgnoreCase("FALLIDO"))) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El estado debe ser EXITOSO o FALLIDO."));
        }
        return pipelineRepositorio.findById(id).map(pipeline -> {
            String estadoAnterior = pipeline.getEstadoUltimaEjecucion();
            pipeline.setEstadoUltimaEjecucion(estado.toUpperCase());
            pipeline.setFechaUltimaEjecucion(LocalDateTime.now());
            Pipelinecicd guardado = pipelineRepositorio.save(pipeline);
            auditoriaServicio.registrar(null, auth.getName(), "RESULTADO",
                    "pipelines", id, estadoAnterior,
                    estado.toUpperCase(), request.getRemoteAddr());
            return ResponseEntity.ok((Object) guardado);
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Pipeline no encontrado: " + id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminar(@PathVariable String id,
                                      Authentication auth,
                                      HttpServletRequest request) {
        if (pipelineRepositorio.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Pipeline no encontrado: " + id));
        }
        auditoriaServicio.registrar(null, auth.getName(), "DELETE",
                "pipelines", id, id, null, request.getRemoteAddr());
        pipelineRepositorio.deleteById(id);
        return ResponseEntity.ok(Map.of("mensaje", "Pipeline eliminado correctamente."));
    }
}
