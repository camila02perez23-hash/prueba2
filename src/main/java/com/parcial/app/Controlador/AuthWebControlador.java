package com.parcial.app.Controlador;

import com.parcial.app.DTO.UsuarioRegistroDTO;
import com.parcial.app.Modelo.Usuario;
import com.parcial.app.Repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.time.LocalDateTime;

@Controller
public class AuthWebControlador {
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuarioRegistro", new UsuarioRegistroDTO());
        return "registro";
    }
    
    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute("usuarioRegistro") UsuarioRegistroDTO dto, Model model) {
        
        try {
            if (usuarioRepositorio.findByCorreo(dto.getCorreo()).isPresent()) {
                model.addAttribute("error", "El correo ya está registrado");
                return "registro";
            }
            
            String nombreRol = dto.getRol();
            if (nombreRol == null || nombreRol.isEmpty()) {
                nombreRol = "USUARIO";
            }
            if (nombreRol.startsWith("ROLE_")) {
                nombreRol = nombreRol.substring(5);
            }
            
            Usuario usuario = new Usuario();
            usuario.setNombres(dto.getNombre());
            usuario.setApellidos("");  // Si tu DTO no tiene apellido separado
            usuario.setCorreo(dto.getCorreo());
            usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
            usuario.setRolNombre(nombreRol);  // ✅ CORREGIDO: setRolNombre (con L, no I)
            usuario.setEstado("ACTIVO");
            usuario.setFechaIngreso(LocalDateTime.now());
            usuario.setFechaCreacion(LocalDateTime.now());
            usuario.setFechaActualizacion(LocalDateTime.now());
            
            usuarioRepositorio.save(usuario);
            
            return "redirect:/login?registroExitoso=true";
            
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar: " + e.getMessage());
            return "registro";
        }
    }
}
