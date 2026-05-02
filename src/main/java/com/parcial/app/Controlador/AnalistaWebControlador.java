package com.parcial.app.Controlador;

import com.parcial.app.Modelo.*;
import com.parcial.app.Servicio.AnalistaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/analista")
public class AnalistaWebControlador {
    
    @Autowired
    private AnalistaServicio analistaServicio;
    
    // Vista principal del módulo analista
    @GetMapping
    public String index(Model model) {
        model.addAttribute("titulo", "Módulo de Analista de Negocio");
        return "analista/index";
    }
    
    // ========== RF-AN-01: Entrevistas ==========
    @GetMapping("/entrevistas")
    public String entrevistas(Model model) {
        model.addAttribute("entrevista", new Entrevista());
        return "analista/entrevistas";
    }
    
    @PostMapping("/entrevistas/guardar")
    public String guardarEntrevista(@ModelAttribute Entrevista entrevista) {
        analistaServicio.registrarEntrevista(entrevista);
        return "redirect:/analista/entrevistas?exito=true";
    }
    
    // ========== RF-AN-02: Documento SRS ==========
    @GetMapping("/srs")
    public String srs() {
        return "analista/srs";
    }
    
    @GetMapping("/srs/generar/{proyectoId}")
    @ResponseBody
    public Map<String, Object> generarSRS(@PathVariable String proyectoId) {
        return analistaServicio.generarSRS(proyectoId);
    }
    
    // ========== RF-AN-03: Diagramas BPMN ==========
    @GetMapping("/bpmn")
    public String bpmn(Model model) {
        model.addAttribute("diagrama", new DiagramaBPMN());
        return "analista/bpmn";
    }
    
    @PostMapping("/bpmn/guardar")
    public String guardarBPMN(@ModelAttribute DiagramaBPMN diagrama) {
        analistaServicio.guardarDiagramaBPMN(diagrama);
        return "redirect:/analista/bpmn?exito=true";
    }
    
    @GetMapping("/bpmn/listar/{proyectoId}")
    @ResponseBody
    public List<DiagramaBPMN> listarBPMN(@PathVariable String proyectoId) {
        return analistaServicio.listarDiagramasBPMN(proyectoId);
    }
    
    // ========== RF-AN-04: Casos de Uso ==========
    @GetMapping("/casos-uso")
    public String casosUso(Model model) {
        model.addAttribute("casoUso", new CasoUso());
        return "analista/casos-uso";
    }
    
    @PostMapping("/casos-uso/guardar")
    public String guardarCasoUso(@ModelAttribute CasoUso casoUso) {
        analistaServicio.crearCasoUso(casoUso);
        return "redirect:/analista/casos-uso?exito=true";
    }
    
    @GetMapping("/casos-uso/listar/{proyectoId}")
    @ResponseBody
    public List<CasoUso> listarCasosUso(@PathVariable String proyectoId) {
        return analistaServicio.listarCasosUso(proyectoId);
    }
    
    // ========== RF-AN-05: Matriz de Trazabilidad ==========
    @GetMapping("/trazabilidad")
    public String trazabilidad() {
        return "analista/trazabilidad";
    }
    
    @GetMapping("/trazabilidad/generar/{proyectoId}")
    @ResponseBody
    public Map<String, Object> generarTrazabilidad(@PathVariable String proyectoId) {
        return analistaServicio.generarMatrizTrazabilidad(proyectoId);
    }
    
    // ========== RF-AN-06: Historias Detalladas ==========
    @GetMapping("/refinar-historia")
    public String refinarHistoria() {
        return "analista/refinar-historia";
    }
    
    @PostMapping("/refinar-historia/guardar")
    @ResponseBody
    public Map<String, Object> guardarRefinamiento(@RequestBody Map<String, Object> payload) {
        String historiaId = (String) payload.get("historiaId");
        String reglasNegocio = (String) payload.get("reglasNegocio");
        Map<String, String> criteriosInvest = (Map<String, String>) payload.get("criteriosINVEST");
        return analistaServicio.refinarHistoria(historiaId, reglasNegocio, criteriosInvest);
    }
    
    // ========== RF-AN-07: Glosario ==========
    @GetMapping("/glosario")
    public String glosario(Model model) {
        model.addAttribute("termino", new GlosarioTermino());
        return "analista/glosario";
    }
    
    @PostMapping("/glosario/guardar")
    public String guardarTermino(@ModelAttribute GlosarioTermino termino) {
        analistaServicio.agregarTermino(termino);
        return "redirect:/analista/glosario?exito=true";
    }
    
    @GetMapping("/glosario/listar/{proyectoId}")
    @ResponseBody
    public List<GlosarioTermino> listarGlosario(@PathVariable String proyectoId) {
        return analistaServicio.listarGlosario(proyectoId);
    }
    
    // ========== RF-AN-08: Validación con Cliente ==========
    @GetMapping("/validacion")
    public String validacion() {
        return "analista/validacion";
    }
    
    @PostMapping("/validacion/enviar")
    @ResponseBody
    public ValidacionRequisito enviarValidacion(@RequestBody Map<String, String> payload) {
        return analistaServicio.enviarValidacion(payload.get("requisitoId"), payload.get("clienteId"));
    }
    
    @PostMapping("/validacion/aprobar/{validacionId}")
    @ResponseBody
    public ValidacionRequisito aprobarValidacion(@PathVariable String validacionId, @RequestParam(required = false) String firma) {
        String firmaElectronica = firma != null ? firma : "Firma electrónica: " + java.time.LocalDateTime.now();
        return analistaServicio.aprobarValidacion(validacionId, firmaElectronica);
    }
    
    @PostMapping("/validacion/rechazar/{validacionId}")
    @ResponseBody
    public ValidacionRequisito rechazarValidacion(@PathVariable String validacionId, @RequestParam String observacion) {
        return analistaServicio.rechazarValidacion(validacionId, observacion);
    }
}