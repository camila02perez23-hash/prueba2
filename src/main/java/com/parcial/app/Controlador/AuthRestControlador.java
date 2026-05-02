package com.parcial.app.Controlador;

import com.parcial.app.DTO.UsuarioRegistroDTO;
import com.parcial.app.Modelo.Usuario;
import com.parcial.app.Repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthRestControlador {
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody UsuarioRegistroDTO dto) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validar si el correo ya existe
            if (usuarioRepositorio.findByCorreo(dto.getCorreo()).isPresent()) {
                response.put("error", "El correo ya está registrado");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Crear nuevo usuario
            Usuario nuevo = new Usuario();
            nuevo.setNombres(dto.getNombre());
            nuevo.setCorreo(dto.getCorreo());
            nuevo.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
            nuevo.setEstado("ACTIVO");
            
            Usuario guardado = usuarioRepositorio.save(nuevo);
            
            response.put("mensaje", "Usuario registrado exitosamente");
            response.put("usuarioId", guardado.getId());
            response.put("correo", guardado.getCorreo());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            response.put("error", "Error al registrar: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}