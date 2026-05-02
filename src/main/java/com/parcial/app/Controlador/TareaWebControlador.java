package com.parcial.app.Controlador;

import com.parcial.app.Modelo.Tarea;
import com.parcial.app.Servicio.TareaServicio;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * TareaWebControlador — Controlador MVC (Thymeleaf) para Tareas Técnicas.
 * Roles: Desarrollador FE (RF-FE-01,07), BE (RF-BE-01), QA, DevOps.
 * Base URL: /tareas
 *
 * Contraparte REST: TareaRestControlador  →  /api/tareas
 */
@Controller
@RequestMapping("/tareas")
@PreAuthorize("isAuthenticated()")
public class TareaWebControlador {

    @Autowired
    private TareaServicio tareaServicio;

    private static final List<String> TIPOS  = List.of("FE", "BE", "QA", "DEVOPS");
    private static final List<String> ESTADOS = List.of(
            "PENDIENTE", "EN_PROGRESO", "EN_REVISION", "EN_QA", "COMPLETADA");

    /** GET /tareas/mis-tareas — kanban personal del usuario */
    @GetMapping("/mis-tareas")
    public String misTareas(Authentication auth, Model model) {
        model.addAttribute("tareas", tareaServicio.listarPorAsignado(auth.getName()));
        return "tareas/mis-tareas";
    }

    /** GET /tareas/proyecto/{idProyecto} — tareas del proyecto */
    @GetMapping("/proyecto/{idProyecto}")
    public String porProyecto(@PathVariable String idProyecto, Model model) {
        model.addAttribute("tareas", tareaServicio.listarPorProyecto(idProyecto));
        model.addAttribute("idProyecto", idProyecto);
        return "tareas/lista";
    }

    /** GET /tareas/{id} — detalle de tarea */
    @GetMapping("/{id}")
    public String detalle(@PathVariable String id, Model model) {
        Optional<Tarea> opt = tareaServicio.buscarPorId(id);
        if (opt.isEmpty()) return "redirect:/tareas/mis-tareas?error=notFound";
        model.addAttribute("tarea", opt.get());
        return "tareas/detalle";
    }

    /** GET /tareas/nueva?idProyecto=xxx */
    @GetMapping("/nueva")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER','SCRUM_MASTER')")
    public String nuevaForm(@RequestParam String idProyecto, Model model) {
        Tarea t = new Tarea();
        t.setIdProyecto(idProyecto);
        model.addAttribute("tarea", t);
        model.addAttribute("idProyecto", idProyecto);
        model.addAttribute("tipos", TIPOS);
        model.addAttribute("estados", ESTADOS);
        return "tareas/form";
    }

    /** POST /tareas/guardar */
    @PostMapping("/guardar")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER','SCRUM_MASTER')")
    public String guardar(@ModelAttribute Tarea tarea,
                          Authentication auth,
                          HttpServletRequest request,
                          RedirectAttributes ra) {
        tareaServicio.crear(tarea, auth.getName(), request.getRemoteAddr());
        ra.addFlashAttribute("exito", "Tarea creada correctamente.");
        return "redirect:/tareas/proyecto/" + tarea.getIdProyecto();
    }

    /** GET /tareas/editar/{id} */
    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable String id, Model model) {
        Optional<Tarea> opt = tareaServicio.buscarPorId(id);
        if (opt.isEmpty()) return "redirect:/tareas/mis-tareas?error=notFound";
        model.addAttribute("tarea", opt.get());
        model.addAttribute("tipos", TIPOS);
        model.addAttribute("estados", ESTADOS);
        return "tareas/form";
    }

    /** POST /tareas/actualizar/{id} */
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable String id,
                             @ModelAttribute Tarea datos,
                             Authentication auth,
                             HttpServletRequest request,
                             RedirectAttributes ra) {
        Optional<Tarea> opt = tareaServicio.buscarPorId(id);
        if (opt.isEmpty()) return "redirect:/tareas/mis-tareas?error=notFound";
        datos.setId(id);
        datos.setFechaCreacion(opt.get().getFechaCreacion());
        tareaServicio.crear(datos, auth.getName(), request.getRemoteAddr());
        ra.addFlashAttribute("exito", "Tarea actualizada.");
        return "redirect:/tareas/" + id;
    }

    /** POST /tareas/{id}/estado — cambia estado desde formulario */
    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable String id,
                                @RequestParam String estado,
                                Authentication auth,
                                HttpServletRequest request,
                                RedirectAttributes ra) {
        tareaServicio.cambiarEstado(id, estado.toUpperCase(),
                auth.getName(), request.getRemoteAddr());
        ra.addFlashAttribute("exito", "Estado actualizado a " + estado + ".");
        return "redirect:/tareas/" + id;
    }

    /** POST /tareas/{id}/horas — registro de horas (RF-FE-07) */
    @PostMapping("/{id}/horas")
    public String registrarHoras(@PathVariable String id,
                                 @RequestParam int horas,
                                 Authentication auth,
                                 HttpServletRequest request,
                                 RedirectAttributes ra) {
        tareaServicio.registrarHoras(id, horas, auth.getName(), request.getRemoteAddr());
        ra.addFlashAttribute("exito", horas + " horas registradas.");
        return "redirect:/tareas/" + id;
    }

    /** GET /tareas/eliminar/{id} */
    @GetMapping("/eliminar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER','SCRUM_MASTER')")
    public String eliminar(@PathVariable String id,
                           Authentication auth,
                           HttpServletRequest request,
                           RedirectAttributes ra) {
        Optional<Tarea> opt = tareaServicio.buscarPorId(id);
        if (opt.isEmpty()) return "redirect:/tareas/mis-tareas?error=notFound";
        String idProyecto = opt.get().getIdProyecto();
        tareaServicio.cambiarEstado(id, "ELIMINADA", auth.getName(), request.getRemoteAddr());
        ra.addFlashAttribute("exito", "Tarea eliminada.");
        return "redirect:/tareas/proyecto/" + idProyecto;
    }
}
