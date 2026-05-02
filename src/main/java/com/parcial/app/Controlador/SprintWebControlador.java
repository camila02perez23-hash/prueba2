package com.parcial.app.Controlador;

import com.parcial.app.Modelo.Sprint;
import com.parcial.app.Repositorio.SprintRepositorio;
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
 * SprintWebControlador — Controlador MVC (Thymeleaf) para Sprints.
 * Rol principal: Project Manager / Scrum Master (RF-PM-02, RF-PM-07).
 * Base URL: /sprints
 *
 * Contraparte REST: SprintRestControlador  →  /api/sprints
 */
@Controller
@RequestMapping("/sprints")
@PreAuthorize("isAuthenticated()")
public class SprintWebControlador {

    @Autowired
    private SprintRepositorio sprintRepositorio;

    @Autowired
    private AuditoriaServicio auditoriaServicio;

    /** GET /sprints/proyecto/{idProyecto} — lista de sprints del proyecto */
    @GetMapping("/proyecto/{idProyecto}")
    public String listarPorProyecto(@PathVariable String idProyecto, Model model) {
        model.addAttribute("sprints",
                sprintRepositorio.findByIdProyectoOrderByNumeroAsc(idProyecto));
        model.addAttribute("idProyecto", idProyecto);
        return "sprints/lista";
    }

    /** GET /sprints/{id} — detalle de sprint */
    @GetMapping("/{id}")
    public String detalle(@PathVariable String id, Model model) {
        Optional<Sprint> opt = sprintRepositorio.findById(id);
        if (opt.isEmpty()) return "redirect:/proyectos?error=sprintNotFound";
        model.addAttribute("sprint", opt.get());
        return "sprints/detalle";
    }

    /** GET /sprints/nuevo?idProyecto=xxx — formulario nuevo sprint */
    @GetMapping("/nuevo")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER','SCRUM_MASTER')")
    public String nuevoForm(@RequestParam String idProyecto, Model model) {
        Sprint s = new Sprint();
        s.setIdProyecto(idProyecto);
        model.addAttribute("sprint", s);
        model.addAttribute("idProyecto", idProyecto);
        return "sprints/form";
    }

    /** POST /sprints/guardar — crea un sprint */
    @PostMapping("/guardar")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER','SCRUM_MASTER')")
    public String guardar(@ModelAttribute Sprint sprint,
                          Authentication auth,
                          HttpServletRequest request,
                          RedirectAttributes ra) {
        if (sprint.getFechaFin() != null && sprint.getFechaInicio() != null
                && sprint.getFechaFin().isBefore(sprint.getFechaInicio())) {
            ra.addFlashAttribute("error", "La fecha de fin no puede ser anterior a la de inicio.");
            return "redirect:/sprints/nuevo?idProyecto=" + sprint.getIdProyecto();
        }
        sprint.setFechaCreacion(LocalDateTime.now());
        Sprint guardado = sprintRepositorio.save(sprint);
        auditoriaServicio.registrar(null, auth.getName(), "CREATE",
                "sprints", guardado.getId(),
                null, "Sprint #" + guardado.getNumero(), request.getRemoteAddr());
        ra.addFlashAttribute("exito", "Sprint creado correctamente.");
        return "redirect:/sprints/proyecto/" + sprint.getIdProyecto();
    }

    /** GET /sprints/editar/{id} — formulario edición */
    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER','SCRUM_MASTER')")
    public String editarForm(@PathVariable String id, Model model) {
        Optional<Sprint> opt = sprintRepositorio.findById(id);
        if (opt.isEmpty()) return "redirect:/proyectos?error=sprintNotFound";
        model.addAttribute("sprint", opt.get());
        return "sprints/form";
    }

    /** POST /sprints/actualizar/{id} */
    @PostMapping("/actualizar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER','SCRUM_MASTER')")
    public String actualizar(@PathVariable String id,
                             @ModelAttribute Sprint datos,
                             Authentication auth,
                             HttpServletRequest request,
                             RedirectAttributes ra) {
        Optional<Sprint> opt = sprintRepositorio.findById(id);
        if (opt.isEmpty()) return "redirect:/proyectos?error=sprintNotFound";
        datos.setId(id);
        datos.setFechaCreacion(opt.get().getFechaCreacion());
        sprintRepositorio.save(datos);
        ra.addFlashAttribute("exito", "Sprint actualizado.");
        return "redirect:/sprints/proyecto/" + datos.getIdProyecto();
    }

    /** GET /sprints/iniciar/{id} — cambia estado a EN_CURSO */
    @GetMapping("/iniciar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER','SCRUM_MASTER')")
    public String iniciar(@PathVariable String id,
                          Authentication auth,
                          HttpServletRequest request,
                          RedirectAttributes ra) {
        Optional<Sprint> opt = sprintRepositorio.findById(id);
        if (opt.isEmpty()) return "redirect:/proyectos?error=sprintNotFound";
        Sprint sprint = opt.get();
        if (!"PLANIFICADO".equals(sprint.getEstado())) {
            ra.addFlashAttribute("error", "Solo se puede iniciar un sprint en estado PLANIFICADO.");
            return "redirect:/sprints/" + id;
        }
        sprint.setEstado("EN_CURSO");
        sprintRepositorio.save(sprint);
        auditoriaServicio.registrar(null, auth.getName(), "INICIAR",
                "sprints", id, "PLANIFICADO", "EN_CURSO", request.getRemoteAddr());
        ra.addFlashAttribute("exito", "Sprint iniciado.");
        return "redirect:/sprints/" + id;
    }

    /** GET /sprints/completar/{id} — cambia estado a COMPLETADO */
    @GetMapping("/completar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER','SCRUM_MASTER')")
    public String completar(@PathVariable String id,
                            Authentication auth,
                            HttpServletRequest request,
                            RedirectAttributes ra) {
        Optional<Sprint> opt = sprintRepositorio.findById(id);
        if (opt.isEmpty()) return "redirect:/proyectos?error=sprintNotFound";
        Sprint sprint = opt.get();
        if (!"EN_CURSO".equals(sprint.getEstado())) {
            ra.addFlashAttribute("error", "Solo se puede completar un sprint EN_CURSO.");
            return "redirect:/sprints/" + id;
        }
        sprint.setEstado("COMPLETADO");
        sprintRepositorio.save(sprint);
        auditoriaServicio.registrar(null, auth.getName(), "COMPLETAR",
                "sprints", id, "EN_CURSO", "COMPLETADO", request.getRemoteAddr());
        ra.addFlashAttribute("exito", "Sprint completado.");
        return "redirect:/sprints/proyecto/" + sprint.getIdProyecto();
    }

    /** GET /sprints/eliminar/{id} */
    @GetMapping("/eliminar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER','SCRUM_MASTER')")
    public String eliminar(@PathVariable String id,
                           Authentication auth,
                           HttpServletRequest request,
                           RedirectAttributes ra) {
        Optional<Sprint> opt = sprintRepositorio.findById(id);
        if (opt.isEmpty()) return "redirect:/proyectos?error=sprintNotFound";
        if (!"PLANIFICADO".equals(opt.get().getEstado())) {
            ra.addFlashAttribute("error",
                    "No se puede eliminar un sprint que ya fue iniciado o completado.");
            return "redirect:/sprints/proyecto/" + opt.get().getIdProyecto();
        }
        String idProyecto = opt.get().getIdProyecto();
        auditoriaServicio.registrar(null, auth.getName(), "DELETE",
                "sprints", id, id, null, request.getRemoteAddr());
        sprintRepositorio.deleteById(id);
        ra.addFlashAttribute("exito", "Sprint eliminado.");
        return "redirect:/sprints/proyecto/" + idProyecto;
    }
}
