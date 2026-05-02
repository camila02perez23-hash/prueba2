package com.parcial.app.Controlador;

import com.parcial.app.Modelo.Pipelinecicd;
import com.parcial.app.Repositorio.Pipelinecicdrepositorio;
import com.parcial.app.Servicio.AuditoriaServicio;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * PipelineCicdWebControlador — Controlador MVC (Thymeleaf) para Pipelines CI/CD.
 * Rol principal: DevOps Engineer (RF-DO-01, RF-DO-02, RF-DO-04).
 * Base URL: /pipelines
 *
 * Contraparte REST: PipelineCicdRestControlador  →  /api/pipelines
 */
@Controller
@RequestMapping("/pipelines")
@PreAuthorize("hasAnyRole('ADMIN','DEVOPS')")
public class PipelineCicdWebControlador {

    @Autowired
    private Pipelinecicdrepositorio pipelineRepositorio;

    @Autowired
    private AuditoriaServicio auditoriaServicio;

    /** GET /pipelines — panel principal de pipelines */
    @GetMapping
    public String panel(Model model) {
        model.addAttribute("pipelines", pipelineRepositorio.findAll());
        return "pipelines/lista";
    }

    /** GET /pipelines/proyecto/{idProyecto} */
    @GetMapping("/proyecto/{idProyecto}")
    @PreAuthorize("isAuthenticated()")
    public String porProyecto(@PathVariable String idProyecto, Model model) {
        model.addAttribute("pipelines", pipelineRepositorio.findByIdProyecto(idProyecto));
        model.addAttribute("idProyecto", idProyecto);
        return "pipelines/lista";
    }

    /** GET /pipelines/{id} */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String detalle(@PathVariable String id, Model model) {
        Optional<Pipelinecicd> opt = pipelineRepositorio.findById(id);
        if (opt.isEmpty()) return "redirect:/pipelines?error=notFound";
        model.addAttribute("pipeline", opt.get());
        return "pipelines/detalle";
    }

    /** GET /pipelines/nuevo?idProyecto=xxx */
    @GetMapping("/nuevo")
    public String nuevoForm(@RequestParam String idProyecto, Model model) {
        Pipelinecicd p = new Pipelinecicd();
        p.setIdProyecto(idProyecto);
        model.addAttribute("pipeline", p);
        model.addAttribute("idProyecto", idProyecto);
        return "pipelines/form";
    }

    /** POST /pipelines/guardar */
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Pipelinecicd pipeline,
                          Authentication auth,
                          HttpServletRequest request,
                          RedirectAttributes ra) {
        pipeline.setFechaCreacion(LocalDateTime.now());
        pipeline.setEstadoUltimaEjecucion("PENDIENTE");
        Pipelinecicd guardado = pipelineRepositorio.save(pipeline);
        auditoriaServicio.registrar(null, auth.getName(), "CREATE",
                "pipelines", guardado.getId(),
                null, guardado.getNombre(), request.getRemoteAddr());
        ra.addFlashAttribute("exito", "Pipeline creado.");
        return "redirect:/pipelines/" + guardado.getId();
    }

    /** GET /pipelines/editar/{id} */
    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable String id, Model model) {
        Optional<Pipelinecicd> opt = pipelineRepositorio.findById(id);
        if (opt.isEmpty()) return "redirect:/pipelines?error=notFound";
        model.addAttribute("pipeline", opt.get());
        return "pipelines/form";
    }

    /** POST /pipelines/actualizar/{id} */
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable String id,
                             @ModelAttribute Pipelinecicd datos,
                             Authentication auth,
                             HttpServletRequest request,
                             RedirectAttributes ra) {
        Optional<Pipelinecicd> opt = pipelineRepositorio.findById(id);
        if (opt.isEmpty()) return "redirect:/pipelines?error=notFound";
        datos.setId(id);
        datos.setFechaCreacion(opt.get().getFechaCreacion());
        pipelineRepositorio.save(datos);
        ra.addFlashAttribute("exito", "Pipeline actualizado.");
        return "redirect:/pipelines/" + id;
    }

    /** GET /pipelines/{id}/ejecutar — dispara el pipeline */
    @GetMapping("/{id}/ejecutar")
    public String ejecutar(@PathVariable String id,
                           Authentication auth,
                           HttpServletRequest request,
                           RedirectAttributes ra) {
        Optional<Pipelinecicd> opt = pipelineRepositorio.findById(id);
        if (opt.isEmpty()) return "redirect:/pipelines?error=notFound";
        Pipelinecicd pipeline = opt.get();
        pipeline.setEstadoUltimaEjecucion("EN_EJECUCION");
        pipeline.setFechaUltimaEjecucion(LocalDateTime.now());
        pipelineRepositorio.save(pipeline);
        auditoriaServicio.registrar(null, auth.getName(), "EJECUTAR",
                "pipelines", id, "PENDIENTE", "EN_EJECUCION", request.getRemoteAddr());
        ra.addFlashAttribute("exito", "Pipeline ejecutado.");
        return "redirect:/pipelines/" + id;
    }

    /** POST /pipelines/{id}/resultado */
    @PostMapping("/{id}/resultado")
    public String actualizarResultado(@PathVariable String id,
                                      @RequestParam String estado,
                                      Authentication auth,
                                      HttpServletRequest request,
                                      RedirectAttributes ra) {
        Optional<Pipelinecicd> opt = pipelineRepositorio.findById(id);
        if (opt.isEmpty()) return "redirect:/pipelines?error=notFound";
        Pipelinecicd pipeline = opt.get();
        pipeline.setEstadoUltimaEjecucion(estado.toUpperCase());
        pipeline.setFechaUltimaEjecucion(LocalDateTime.now());
        pipelineRepositorio.save(pipeline);
        ra.addFlashAttribute("exito", "Resultado actualizado.");
        return "redirect:/pipelines/" + id;
    }

    /** GET /pipelines/eliminar/{id} */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id,
                           Authentication auth,
                           HttpServletRequest request,
                           RedirectAttributes ra) {
        if (pipelineRepositorio.findById(id).isEmpty())
            return "redirect:/pipelines?error=notFound";
        auditoriaServicio.registrar(null, auth.getName(), "DELETE",
                "pipelines", id, id, null, request.getRemoteAddr());
        pipelineRepositorio.deleteById(id);
        ra.addFlashAttribute("exito", "Pipeline eliminado.");
        return "redirect:/pipelines";
    }
}
