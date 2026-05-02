package com.parcial.app.Servicio;

import com.parcial.app.Modelo.*;
import com.parcial.app.Repositorio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AnalistaServicio {
    
    @Autowired
    private RequisitoRepositorio requisitoRepo;
    
    @Autowired
    private CasoUsoRepositorio casoUsoRepo;
    
    @Autowired
    private GlosarioRepositorio glosarioRepo;
    
    @Autowired
    private EntrevistaRepositorio entrevistaRepo;
    
    @Autowired
    private DiagramaBPMNRepositorio bpmnRepo;
    
    @Autowired
    private ValidacionRepositorio validacionRepo;
    
    // ========== RF-AN-01: Entrevistas ==========
    public Entrevista registrarEntrevista(Entrevista entrevista) {
        entrevista.setFecha(LocalDateTime.now());
        return entrevistaRepo.save(entrevista);
    }
    
    // ========== RF-AN-02: Generar SRS ==========
    public Map<String, Object> generarSRS(String proyectoId) {
        Map<String, Object> srs = new HashMap<>();
        srs.put("proyectoId", proyectoId);
        srs.put("fechaGeneracion", LocalDateTime.now());
        srs.put("requisitos", requisitoRepo.findByProyectoId(proyectoId));
        srs.put("estandar", "IEEE 830");
        return srs;
    }
    
    // ========== RF-AN-03: Diagramas BPMN ==========
    public DiagramaBPMN guardarDiagramaBPMN(DiagramaBPMN diagrama) {
        diagrama.setFechaCreacion(LocalDateTime.now());
        return bpmnRepo.save(diagrama);
    }
    
    public List<DiagramaBPMN> listarDiagramasBPMN(String proyectoId) {
        return bpmnRepo.findByProyectoId(proyectoId);
    }
    
    // ========== RF-AN-04: Casos de Uso ==========
    public CasoUso crearCasoUso(CasoUso casoUso) {
        casoUso.setFechaCreacion(LocalDateTime.now());
        return casoUsoRepo.save(casoUso);
    }
    
    public List<CasoUso> listarCasosUso(String proyectoId) {
        return casoUsoRepo.findByProyectoId(proyectoId);
    }
    
    // ========== RF-AN-05: Matriz de Trazabilidad ==========
    public Map<String, Object> generarMatrizTrazabilidad(String proyectoId) {
        Map<String, Object> matriz = new HashMap<>();
        matriz.put("proyectoId", proyectoId);
        matriz.put("requisitos", requisitoRepo.findByProyectoId(proyectoId));
        matriz.put("casosUso", casoUsoRepo.findByProyectoId(proyectoId));
        matriz.put("fechaGeneracion", LocalDateTime.now());
        return matriz;
    }
    
    // ========== RF-AN-06: Refinar Historia ==========
    public Map<String, Object> refinarHistoria(String historiaId, String reglasNegocio, Map<String, String> criteriosInvest) {
        Map<String, Object> refinamiento = new HashMap<>();
        refinamiento.put("historiaId", historiaId);
        refinamiento.put("reglasNegocio", reglasNegocio);
        refinamiento.put("criteriosINVEST", criteriosInvest);
        refinamiento.put("fechaRefinamiento", LocalDateTime.now());
        return refinamiento;
    }
    
    // ========== RF-AN-07: Glosario ==========
    public GlosarioTermino agregarTermino(GlosarioTermino termino) {
        termino.setFechaCreacion(LocalDateTime.now());
        return glosarioRepo.save(termino);
    }
    
    public List<GlosarioTermino> listarGlosario(String proyectoId) {
        return glosarioRepo.findByProyectoId(proyectoId);
    }
    
    // ========== RF-AN-08: Validación con Cliente ==========
    public ValidacionRequisito enviarValidacion(String requisitoId, String clienteId) {
        ValidacionRequisito validacion = new ValidacionRequisito();
        validacion.setRequisitoId(requisitoId);
        validacion.setClienteId(clienteId);
        validacion.setEstado("PENDIENTE");
        validacion.setFechaSolicitud(LocalDateTime.now());
        return validacionRepo.save(validacion);
    }
    
    public ValidacionRequisito aprobarValidacion(String validacionId, String firmaElectronica) {
        ValidacionRequisito validacion = validacionRepo.findById(validacionId)
            .orElseThrow(() -> new RuntimeException("Validación no encontrada"));
        validacion.setEstado("APROBADO");
        validacion.setFirmaElectronica(firmaElectronica);
        validacion.setFechaRespuesta(LocalDateTime.now());
        
        // Actualizar el estado del requisito
        Optional<Requisito> requisitoOpt = requisitoRepo.findById(validacion.getRequisitoId());
        if (requisitoOpt.isPresent()) {
            Requisito requisito = requisitoOpt.get();
            requisito.setEstado("APROBADO");
            requisitoRepo.save(requisito);
        }
        
        return validacionRepo.save(validacion);
    }
    
    public ValidacionRequisito rechazarValidacion(String validacionId, String observacion) {
        ValidacionRequisito validacion = validacionRepo.findById(validacionId)
            .orElseThrow(() -> new RuntimeException("Validación no encontrada"));
        validacion.setEstado("RECHAZADO");
        validacion.setObservacion(observacion);
        validacion.setFechaRespuesta(LocalDateTime.now());
        
        Optional<Requisito> requisitoOpt = requisitoRepo.findById(validacion.getRequisitoId());
        if (requisitoOpt.isPresent()) {
            Requisito requisito = requisitoOpt.get();
            requisito.setEstado("RECHAZADO");
            requisitoRepo.save(requisito);
        }
        
        return validacionRepo.save(validacion);
    }
}