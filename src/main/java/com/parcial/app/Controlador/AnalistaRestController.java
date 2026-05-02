package com.parcial.app.Controlador;

import com.parcial.app.Modelo.*;
import com.parcial.app.Servicio.AnalistaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analista")
public class AnalistaRestController {
    
    @Autowired
    private AnalistaServicio analistaServicio;
    
    // RF-AN-01
    @PostMapping("/entrevistas")
    public ResponseEntity<Entrevista> registrarEntrevista(@RequestBody Entrevista entrevista) {
        return ResponseEntity.ok(analistaServicio.registrarEntrevista(entrevista));
    }
    
    // RF-AN-02
    @GetMapping("/srs/{proyectoId}")
    public ResponseEntity<Map<String, Object>> generarSRS(@PathVariable String proyectoId) {
        return ResponseEntity.ok(analistaServicio.generarSRS(proyectoId));
    }
    
    // RF-AN-03
    @PostMapping("/bpmn")
    public ResponseEntity<DiagramaBPMN> guardarDiagramaBPMN(@RequestBody DiagramaBPMN diagrama) {
        return ResponseEntity.ok(analistaServicio.guardarDiagramaBPMN(diagrama));
    }
    
    @GetMapping("/bpmn/proyecto/{proyectoId}")
    public ResponseEntity<List<DiagramaBPMN>> listarDiagramasBPMN(@PathVariable String proyectoId) {
        return ResponseEntity.ok(analistaServicio.listarDiagramasBPMN(proyectoId));
    }
    
    // RF-AN-04
    @PostMapping("/casos-uso")
    public ResponseEntity<CasoUso> crearCasoUso(@RequestBody CasoUso casoUso) {
        return ResponseEntity.ok(analistaServicio.crearCasoUso(casoUso));
    }
    
    @GetMapping("/casos-uso/proyecto/{proyectoId}")
    public ResponseEntity<List<CasoUso>> listarCasosUso(@PathVariable String proyectoId) {
        return ResponseEntity.ok(analistaServicio.listarCasosUso(proyectoId));
    }
    
    // RF-AN-05
    @GetMapping("/trazabilidad/{proyectoId}")
    public ResponseEntity<Map<String, Object>> generarMatrizTrazabilidad(@PathVariable String proyectoId) {
        return ResponseEntity.ok(analistaServicio.generarMatrizTrazabilidad(proyectoId));
    }
    
    // RF-AN-06
    @PostMapping("/refinar-historia")
    public ResponseEntity<Map<String, Object>> refinarHistoria(@RequestBody Map<String, Object> payload) {
        String historiaId = (String) payload.get("historiaId");
        String reglasNegocio = (String) payload.get("reglasNegocio");
        Map<String, String> criteriosInvest = (Map<String, String>) payload.get("criteriosINVEST");
        return ResponseEntity.ok(analistaServicio.refinarHistoria(historiaId, reglasNegocio, criteriosInvest));
    }
    
    // RF-AN-07
    @PostMapping("/glosario")
    public ResponseEntity<GlosarioTermino> agregarTermino(@RequestBody GlosarioTermino termino) {
        return ResponseEntity.ok(analistaServicio.agregarTermino(termino));
    }
    
    @GetMapping("/glosario/proyecto/{proyectoId}")
    public ResponseEntity<List<GlosarioTermino>> listarGlosario(@PathVariable String proyectoId) {
        return ResponseEntity.ok(analistaServicio.listarGlosario(proyectoId));
    }
    
    // RF-AN-08
    @PostMapping("/validacion/enviar")
    public ResponseEntity<ValidacionRequisito> enviarValidacion(@RequestBody Map<String, String> payload) {
        return ResponseEntity.ok(analistaServicio.enviarValidacion(payload.get("requisitoId"), payload.get("clienteId")));
    }
    
    @PatchMapping("/validacion/{validacionId}/aprobar")
    public ResponseEntity<ValidacionRequisito> aprobarValidacion(@PathVariable String validacionId, @RequestBody(required = false) Map<String, String> payload) {
        String firma = payload != null ? payload.getOrDefault("firma", "Firma electrónica: " + LocalDateTime.now()) : "Firma electrónica";
        return ResponseEntity.ok(analistaServicio.aprobarValidacion(validacionId, firma));
    }
    
    @PatchMapping("/validacion/{validacionId}/rechazar")
    public ResponseEntity<ValidacionRequisito> rechazarValidacion(@PathVariable String validacionId, @RequestBody Map<String, String> payload) {
        String observacion = payload.getOrDefault("observacion", "Sin observaciones");
        return ResponseEntity.ok(analistaServicio.rechazarValidacion(validacionId, observacion));
    }
}