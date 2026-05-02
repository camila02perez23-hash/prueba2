package com.parcial.app.Controlador;

import com.parcial.app.Modelo.Defecto;
import com.parcial.app.Servicio.DefectoServicio;
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
 * DefectoWebControlador — Controlador MVC (Thymeleaf) para Defectos.
 * Rol principal: QA Engineer (RF-QA-04, RF-QA-05) y Frontend (RF-FE-08).
 * Ciclo de vida: NUEVO → ASIGNADO → EN_DESARROLLO → RESUELTO → EN_VERIFICACION → CERRADO | REABIERTO
 * Base URL: /defectos
 *
 * Contraparte REST: DefectoRestControlador  →  /api/defectos
 */
@Controller
@RequestMapping("/defectos")
@PreAuthorize("isAuthenticated()")
public class DefectoWebControlador {

    @Autowired
    private DefectoServicio defectoServicio;

    private static final List<String> SEVERIDADES =
            List.of("CRITICA", "ALTA", "MEDIA", "BAJA");
    private static final List<String> ESTADOS =
            List.of("NUEVO", "ASIGNADO", "EN_DESARROLLO",
                    "RESUELTO", "EN_VERIFICACION", "CERRADO", "REABIERTO");

    /** GET /defectos/proyecto/{idProyecto} — gestor de defectos del proyecto */
    @GetMapping("/proyecto/{idProyecto}")
    public String porProyecto(@PathVariable String idProyecto, Model model) {
        model.addAttribute("defectos", defectoServicio.listarPorProyecto(idProyecto));
        model.addAttribute("idProyecto", idProyecto);
        model.addAttribute("abiertos", defectoServicio.contarAbiertos(idProyecto));
        return "defectos/lista";
    }

    /** GET /defectos/{id} — detalle del defecto */
    @GetMapping("/{id}")
    public String detalle(@PathVariable String id, Model model) {
        Optional<Defecto> opt = defectoServicio.buscarPorId(id);
        if (opt.isEmpty()) return "redirect:/tareas/mis-tareas?error=defectoNotFound";
        model.addAttribute("defecto", opt.get());
        model.addAttribute("estados", ESTADOS);
        return "defectos/detalle";
    }

    /** GET /defectos/nuevo?idProyecto=xxx — formulario reporte de bug (RF-FE-08 / RF-QA-04) */
    @GetMapping("/nuevo")
    public String nuevoForm(@RequestParam String idProyecto, Model model) {
        Defecto d = new Defecto();
        d.setIdProyecto(idProyecto);
        model.addAttribute("defecto", d);
        model.addAttribute("idProyecto", idProyecto);
        model.addAttribute("severidades", SEVERIDADES);
        return "defectos/form";
    }

    /** POST /defectos/guardar */
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Defecto defecto,
                          Authentication auth,
                          HttpServletRequest request,
                          RedirectAttributes ra) {
        defecto.setIdReportadoPor(auth.getName());
        defectoServicio.reportar(defecto, auth.getName(), request.getRemoteAddr());
        ra.addFlashAttribute("exito", "Defecto reportado correctamente.");
        return "redirect:/defectos/proyecto/" + defecto.getIdProyecto();
    }

    /** POST /defectos/{id}/estado — transición de estado */
    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable String id,
                                @RequestParam String estado,
                                Authentication auth,
                                HttpServletRequest request,
                                RedirectAttributes ra) {
        defectoServicio.cambiarEstado(id, estado.toUpperCase(),
                auth.getName(), request.getRemoteAddr());
        ra.addFlashAttribute("exito", "Estado actualizado a " + estado + ".");
        return "redirect:/defectos/" + id;
    }

    /** GET /defectos/{id}/cerrar */
    @GetMapping("/{id}/cerrar")
    @PreAuthorize("hasAnyRole('ADMIN','QA')")
    public String cerrar(@PathVariable String id,
                         Authentication auth,
                         HttpServletRequest request,
                         RedirectAttributes ra) {
        defectoServicio.cambiarEstado(id, "CERRADO", auth.getName(), request.getRemoteAddr());
        ra.addFlashAttribute("exito", "Defecto cerrado.");
        return "redirect:/defectos/" + id;
    }

    /** GET /defectos/{id}/reabrir */
    @GetMapping("/{id}/reabrir")
    @PreAuthorize("hasAnyRole('ADMIN','QA')")
    public String reabrir(@PathVariable String id,
                          Authentication auth,
                          HttpServletRequest request,
                          RedirectAttributes ra) {
        defectoServicio.cambiarEstado(id, "REABIERTO", auth.getName(), request.getRemoteAddr());
        ra.addFlashAttribute("exito", "Defecto reabierto.");
        return "redirect:/defectos/" + id;
    }
}
