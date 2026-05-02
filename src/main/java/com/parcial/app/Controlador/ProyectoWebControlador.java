package com.parcial.app.Controlador;

import com.parcial.app.Modelo.Proyecto;
import com.parcial.app.Servicio.ProyectoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/proyectos")
public class ProyectoWebControlador {
    
    @Autowired
    private ProyectoServicio proyectoServicio;
    
    @GetMapping
    public String listarProyectos(Model model) {
        model.addAttribute("proyectos", proyectoServicio.listarTodos());
        return "proyectos/lista";
    }
    
    @GetMapping("/nuevo")
    public String nuevoProyecto(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        return "proyectos/formulario";
    }
    
    @PostMapping("/guardar")
    public String guardarProyecto(@ModelAttribute Proyecto proyecto) {
        proyectoServicio.crear(proyecto, "admin", "127.0.0.1");
        return "redirect:/proyectos";
    }
    
    @GetMapping("/editar/{id}")
    public String editarProyecto(@PathVariable String id, Model model) {
        proyectoServicio.buscarPorId(id).ifPresent(p -> model.addAttribute("proyecto", p));
        return "proyectos/formulario";
    }
    
    @GetMapping("/exportar/pdf")
    public String exportarPDF() {
        return "redirect:/api/exportar/proyectos/pdf";
    }
}