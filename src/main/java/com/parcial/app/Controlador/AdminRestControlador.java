package com.parcial.app.Controlador;

import com.parcial.app.DTO.UsuarioCreacionDTO;
import com.parcial.app.Excepcion.PermisoNotFoundException;
import com.parcial.app.Modelo.Permiso;
import com.parcial.app.Modelo.Rol;
import com.parcial.app.Modelo.Usuario;
import com.parcial.app.Repositorio.RolRepositorio;
import com.parcial.app.Servicio.AdminServicio;
import com.parcial.app.Servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminRestControlador {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Autowired
    private AdminServicio adminServicio;
    
    @Autowired
    private RolRepositorio rolRepositorio;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // ========== USUARIOS ==========
    
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioServicio.listarTodos());
    }
    
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable String id) {
        return usuarioServicio.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/usuarios")
    public ResponseEntity<?> crearUsuario(@RequestBody Map<String, Object> datos) {
        try {
            String nombres = (String) datos.get("nombres");
            String apellidos = (String) datos.get("apellidos");
            String correo = (String) datos.get("correo");
            String password = (String) datos.get("password");
            String rolNombre = (String) datos.get("rolNombre");
            
            if (rolNombre == null) {
                rolNombre = (String) datos.get("rol");
            }
            
            Usuario usuario = new Usuario();
            usuario.setNombres(nombres);
            usuario.setApellidos(apellidos);
            usuario.setCorreo(correo);
            usuario.setPasswordHash(passwordEncoder.encode(password));
            usuario.setRolNombre(rolNombre);  // ✅ CORREGIDO
            usuario.setEstado("ACTIVO");
            
            Usuario nuevo = usuarioServicio.guardar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // ========== ROLES ==========
    
    @GetMapping("/roles")
    public ResponseEntity<List<Rol>> listarRoles() {
        return ResponseEntity.ok(adminServicio.listarRoles());
    }
    
    @GetMapping("/roles/{id}")
    public ResponseEntity<Rol> obtenerRol(@PathVariable String id) {
        return ResponseEntity.ok(adminServicio.obtenerRolPorId(id));
    }
    
    @PostMapping("/roles")
    public ResponseEntity<Rol> crearRol(@RequestBody Rol rol) {
        Rol nuevoRol = adminServicio.crearRol(rol);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRol);
    }
    
    @PutMapping("/roles/{id}")
    public ResponseEntity<Rol> actualizarRol(@PathVariable String id, @RequestBody Rol rol) {
        Rol rolActualizado = adminServicio.actualizarRol(id, rol);
        return ResponseEntity.ok(rolActualizado);
    }
    
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> eliminarRol(@PathVariable String id) {
        adminServicio.eliminarRol(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/roles/{rolId}/permisos/{permisoId}")
    public ResponseEntity<Rol> asignarPermiso(
            @PathVariable String rolId,
            @PathVariable String permisoId) throws PermisoNotFoundException {
        Rol rol = adminServicio.asignarPermisoARol(rolId, permisoId);
        return ResponseEntity.ok(rol);
    }
    
    @DeleteMapping("/roles/{rolId}/permisos/{permisoId}")
    public ResponseEntity<Rol> removerPermiso(
            @PathVariable String rolId,
            @PathVariable String permisoId) throws PermisoNotFoundException {
        Rol rol = adminServicio.removerPermisoDeRol(rolId, permisoId);
        return ResponseEntity.ok(rol);
    }
    
    // ========== PERMISOS ==========
    
    @GetMapping("/permisos")
    public ResponseEntity<List<Permiso>> listarPermisos() {
        return ResponseEntity.ok(adminServicio.listarPermisos());
    }
    
    @GetMapping("/permisos/{id}")
    public ResponseEntity<Permiso> obtenerPermiso(@PathVariable String id) {
        return ResponseEntity.ok(adminServicio.obtenerPermisoPorId(id));
    }
    
    @GetMapping("/permisos/modulo/{modulo}")
    public ResponseEntity<List<Permiso>> listarPermisosPorModulo(@PathVariable String modulo) {
        return ResponseEntity.ok(adminServicio.listarPermisosPorModulo(modulo));
    }
    
    @PostMapping("/permisos")
    public ResponseEntity<Permiso> crearPermiso(@RequestBody Permiso permiso) {
        Permiso nuevoPermiso = adminServicio.crearPermiso(permiso);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPermiso);
    }
    
    @PutMapping("/permisos/{id}")
    public ResponseEntity<Permiso> actualizarPermiso(@PathVariable String id, @RequestBody Permiso permiso) {
        Permiso permisoActualizado = adminServicio.actualizarPermiso(id, permiso);
        return ResponseEntity.ok(permisoActualizado);
    }
    
    @DeleteMapping("/permisos/{id}")
    public ResponseEntity<Void> eliminarPermiso(@PathVariable String id) {
        adminServicio.eliminarPermiso(id);
        return ResponseEntity.noContent().build();
    }
}