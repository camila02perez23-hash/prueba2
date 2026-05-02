package com.parcial.app.Controlador;

import com.parcial.app.Modelo.HistoriaUsuario;
import com.parcial.app.Repositorio.HistoriaUsuarioRepositorio;
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
import java.util.List;
import java.util.Optional;

/**
 * HistoriaUsuarioWebControlador — Controlador MVC (Thymeleaf) para Historias de Usuario.
 * Rol principal: Product Owner (RF-PO-01..07) y Analista de Negocio (RF-AN-06).
 * Base URL: /historias
 *
 * Contraparte REST: HistoriaUsuarioRestControlador  →  /api/historias
 */
@Controller
@RequestMapping("/historias")
@PreAuthorize("isAuthenticated()")
public class HistoriaUsuarioWebControlador {

    @Autowired
    private HistoriaUsuarioRepositorio historiaRepositorio;

    @Autowired
    private AuditoriaServicio auditoriaServicio;

    private static final List<String> PRIORIDADES_MOSCOW =
            List.of("MUST", "SHOULD", "COULD", "WONT");

    private static final List<String> ESTADOS =
            List.of("PENDIENTE", "EN_DESARROLLO", "PARA_ACEPTACION", "ACEPTADA", "RECHAZADA");

    /** GET /historias/proyecto/{idProyecto} — backlog del proyecto */
    @GetMapping("/proyecto/{idProyecto}")
    public String backlog(@PathVariable String idProyecto, Model model) {
        model.addAttribute("historias",
                historiaRepositorio.findByIdProyecto(idProyecto));
        model.addAttribute("idProyecto", idProyecto);
        return "historias/lista";
    }

    /** GET /historias/proyecto/{idProyecto}/aceptacion — tablero de aceptación (RF-PO-04) */
    @GetMapping("/proyecto/{idProyecto}/aceptacion")
    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER')")
    public String tableroAceptacion(@PathVariable String idProyecto, Model model) {
        model.addAttribute("historias",
                historiaRepositorio.findByIdProyectoAndEstado(idProyecto, "PARA_ACEPTACION"));
        model.addAttribute("idProyecto", idProyecto);
        return "historias/aceptacion";
    }

    /** GET /historias/{id} — detalle de historia */
    @GetMapping("/{id}")
    public String detalle(@PathVariable String id, Model model) {
        Optional<HistoriaUsuario> opt = historiaRepositorio.findById(id);
        if (opt.isEmpty()) return "redirect:/proyectos?error=historiaNotFound";
        model.addAttribute("historia", opt.get());
        return "historias/detalle";
    }

    /** GET /historias/nueva?idProyecto=xxx — formulario nueva historia */
    @GetMapping("/nueva")
    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER','ANALISTA')")
    public String nuevaForm(@RequestParam String idProyecto, Model model) {
        HistoriaUsuario h = new HistoriaUsuario();
        h.setIdProyecto(idProyecto);
        model.addAttribute("historia", h);
        model.addAttribute("idProyecto", idProyecto);
        model.addAttribute("prioridades", PRIORIDADES_MOSCOW);
        model.addAttribute("estados", ESTADOS);
        return "historias/form";
    }

    /** POST /historias/guardar */
    @PostMapping("/guardar")
    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER','ANALISTA')")
    public String guardar(@ModelAttribute HistoriaUsuario historia,
                          Authentication auth,
                          HttpServletRequest request,
                          RedirectAttributes ra) {
        historia.setFechaCreacion(LocalDateTime.now());
        HistoriaUsuario guardada = historiaRepositorio.save(historia);
        auditoriaServicio.registrar(null, auth.getName(), "CREATE",
                "historias_usuario", guardada.getId(),
                null, guardada.getTitulo(), request.getRemoteAddr());
        ra.addFlashAttribute("exito", "Historia creada correctamente.");
        return "redirect:/historias/proyecto/" + historia.getIdProyecto();
    }

    /** GET /historias/editar/{id} */
    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER','ANALISTA')")
    public String editarForm(@PathVariable String id, Model model) {
        Optional<HistoriaUsuario> opt = historiaRepositorio.findById(id);
        if (opt.isEmpty()) return "redirect:/proyectos?error=historiaNotFound";
        model.addAttribute("historia", opt.get());
        model.addAttribute("prioridades", PRIORIDADES_MOSCOW);
        model.addAttribute("estados", ESTADOS);
        return "historias/form";
    }

    /** POST /historias/actualizar/{id} */
    @PostMapping("/actualizar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER','ANALISTA')")
    public String actualizar(@PathVariable String id,
                             @ModelAttribute HistoriaUsuario datos,
                             Authentication auth,
                             HttpServletRequest request,
                             RedirectAttributes ra) {
        Optional<HistoriaUsuario> opt = historiaRepositorio.findById(id);
        if (opt.isEmpty()) return "redirect:/proyectos?error=historiaNotFound";
        datos.setId(id);
        datos.setFechaCreacion(opt.get().getFechaCreacion());
        historiaRepositorio.save(datos);
        ra.addFlashAttribute("exito", "Historia actualizada.");
        return "redirect:/historias/" + id;
    }

    /** POST /historias/{id}/aprobar — aprobación formal (RF-PO-04) */
    @PostMapping("/{id}/aprobar")
    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER')")
    public String aprobar(@PathVariable String id,
                          @RequestParam(required = false) String observacion,
                          Authentication auth,
                          HttpServletRequest request,
                          RedirectAttributes ra) {
        Optional<HistoriaUsuario> opt = historiaRepositorio.findById(id);
        if (opt.isEmpty()) return "redirect:/proyectos?error=historiaNotFound";
        HistoriaUsuario h = opt.get();
        h.setEstado("ACEPTADA");
        if (observacion != null && !observacion.isBlank()) h.setObservacionAceptacion(observacion);
        historiaRepositorio.save(h);
        auditoriaServicio.registrar(null, auth.getName(), "APROBAR",
                "historias_usuario", id, "PARA_ACEPTACION", "ACEPTADA", request.getRemoteAddr());
        ra.addFlashAttribute("exito", "Historia aceptada.");
        return "redirect:/historias/proyecto/" + h.getIdProyecto() + "/aceptacion";
    }

    /** POST /historias/{id}/rechazar — rechazo con motivo (RF-PO-04) */
    @PostMapping("/{id}/rechazar")
    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER')")
    public String rechazar(@PathVariable String id,
                           @RequestParam String observacion,
                           Authentication auth,
                           HttpServletRequest request,
                           RedirectAttributes ra) {
        if (observacion == null || observacion.isBlank()) {
            ra.addFlashAttribute("error", "Debe indicar el motivo del rechazo.");
            return "redirect:/historias/" + id;
        }
        Optional<HistoriaUsuario> opt = historiaRepositorio.findById(id);
        if (opt.isEmpty()) return "redirect:/proyectos?error=historiaNotFound";
        HistoriaUsuario h = opt.get();
        h.setEstado("RECHAZADA");
        h.setObservacionAceptacion(observacion);
        historiaRepositorio.save(h);
        auditoriaServicio.registrar(null, auth.getName(), "RECHAZAR",
                "historias_usuario", id, "PARA_ACEPTACION", "RECHAZADA", request.getRemoteAddr());
        ra.addFlashAttribute("exito", "Historia rechazada.");
        return "redirect:/historias/proyecto/" + h.getIdProyecto() + "/aceptacion";
    }

    /** GET /historias/eliminar/{id} */
    @GetMapping("/eliminar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER')")
    public String eliminar(@PathVariable String id,
                           Authentication auth,
                           HttpServletRequest request,
                           RedirectAttributes ra) {
        Optional<HistoriaUsuario> opt = historiaRepositorio.findById(id);
        if (opt.isEmpty()) return "redirect:/proyectos?error=historiaNotFound";
        String idProyecto = opt.get().getIdProyecto();
        auditoriaServicio.registrar(null, auth.getName(), "DELETE",
                "historias_usuario", id, id, null, request.getRemoteAddr());
        historiaRepositorio.deleteById(id);
        ra.addFlashAttribute("exito", "Historia eliminada.");
        return "redirect:/historias/proyecto/" + idProyecto;
    }
}
