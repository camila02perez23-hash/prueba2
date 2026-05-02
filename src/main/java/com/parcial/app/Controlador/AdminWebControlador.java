package com.parcial.app.Controlador;

import com.parcial.app.Modelo.Permiso;
import com.parcial.app.Modelo.Rol;
import com.parcial.app.Modelo.Usuario;
import com.parcial.app.Servicio.AdminServicio;
import com.parcial.app.Servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminWebControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Autowired
    private AdminServicio adminServicio;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Usuario> todosUsuarios = usuarioServicio.listarTodos();
        long totalUsuarios = todosUsuarios.size();
        long activos = todosUsuarios.stream()
            .filter(u -> "ACTIVO".equals(u.getEstado()))
            .count();
        
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("usuariosActivos", activos);
        model.addAttribute("totalRoles", adminServicio.contarRoles());
        model.addAttribute("totalPermisos", adminServicio.contarPermisos());
        model.addAttribute("usuarios", todosUsuarios);
        model.addAttribute("roles", adminServicio.listarRoles());
        
        return "admin/dashboard";
    }
    
    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("usuarios", usuarioServicio.listarTodos());
        return "admin/usuarios";
    }
    
    // ========== USUARIOS CRUD ==========
    
    @GetMapping("/usuarios/nuevo")
    public String nuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", adminServicio.listarRoles());
        return "admin/usuario-form";
    }
    
    @PostMapping("/usuarios/nuevo")
    public String crearUsuario(@RequestParam String nombres,
                                @RequestParam String apellidos,
                                @RequestParam String correo,
                                @RequestParam String password,
                                @RequestParam String rolNombre) {
        Usuario usuario = new Usuario();
        usuario.setNombres(nombres);
        usuario.setApellidos(apellidos);
        usuario.setCorreo(correo);
        usuario.setPasswordHash(new BCryptPasswordEncoder().encode(password));
        usuario.setRolNombre(rolNombre);
        usuario.setEstado("ACTIVO");
        usuarioServicio.guardar(usuario);
        return "redirect:/admin/usuarios";
    }
    
    @GetMapping("/usuarios/{id}/editar")
    public String editarUsuario(@PathVariable String id, Model model) {
        Optional<Usuario> usuarioOpt = usuarioServicio.obtenerPorId(id);
        if (usuarioOpt.isPresent()) {
            model.addAttribute("usuario", usuarioOpt.get());
            model.addAttribute("roles", adminServicio.listarRoles());
            return "admin/usuario-form";
        }
        return "redirect:/admin/usuarios";
    }
    
    @PostMapping("/usuarios/actualizar/{id}")
    public String actualizarUsuario(@PathVariable String id,
                                     @RequestParam String nombres,
                                     @RequestParam String apellidos,
                                     @RequestParam String correo,
                                     @RequestParam String rolNombre,
                                     @RequestParam String estado) {
        Optional<Usuario> usuarioOpt = usuarioServicio.obtenerPorId(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setNombres(nombres);
            usuario.setApellidos(apellidos);
            usuario.setCorreo(correo);
            usuario.setRolNombre(rolNombre);
            usuario.setEstado(estado);
            usuarioServicio.guardar(usuario);
        }
        return "redirect:/admin/usuarios";
    }
    
    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable String id) {
        usuarioServicio.eliminar(id);
        return "redirect:/admin/usuarios";
    }
    
    // ========== ROLES CRUD ==========
    
    @GetMapping("/roles")
    public String roles(Model model) {
        model.addAttribute("roles", adminServicio.listarRoles());
        return "admin/roles";
    }
    
    @GetMapping("/roles/nuevo")
    public String nuevoRol(Model model) {
        model.addAttribute("rol", new Rol());
        return "admin/rol-form";
    }
    
    @PostMapping("/roles/guardar")
    public String guardarRol(@RequestParam String nombre,
                              @RequestParam String descripcion,
                              @RequestParam Integer nivelAcceso) {
        Rol rol = new Rol();
        rol.setNombre(nombre);
        rol.setDescripcion(descripcion);
        rol.setNivelAcceso(nivelAcceso);
        adminServicio.crearRol(rol);
        return "redirect:/admin/roles";
    }
    
    @GetMapping("/roles/editar/{id}")
    public String editarRol(@PathVariable String id, Model model) {
        Rol rol = adminServicio.obtenerRolPorId(id);
        model.addAttribute("rol", rol);
        return "admin/rol-form";
    }
    
    @PostMapping("/roles/actualizar/{id}")
    public String actualizarRol(@PathVariable String id,
                                 @RequestParam String nombre,
                                 @RequestParam String descripcion,
                                 @RequestParam Integer nivelAcceso) {
        Rol rol = adminServicio.obtenerRolPorId(id);
        rol.setNombre(nombre);
        rol.setDescripcion(descripcion);
        rol.setNivelAcceso(nivelAcceso);
        adminServicio.actualizarRol(id, rol);
        return "redirect:/admin/roles";
    }
    
    @GetMapping("/roles/eliminar/{id}")
    public String eliminarRol(@PathVariable String id) {
        adminServicio.eliminarRol(id);
        return "redirect:/admin/roles";
    }
    
    // ========== PERMISOS CRUD ==========
    
    @GetMapping("/permisos")
    public String permisos(Model model) {
        model.addAttribute("permisos", adminServicio.listarPermisos());
        return "admin/permisos";
    }
    
    @GetMapping("/permisos/nuevo")
    public String nuevoPermiso(Model model) {
        model.addAttribute("permiso", new Permiso());
        return "admin/permiso-form";
    }
    
    @PostMapping("/permisos/guardar")
    public String guardarPermiso(@RequestParam String nombre,
                                  @RequestParam String descripcion,
                                  @RequestParam String modulo) {
        Permiso permiso = new Permiso();
        permiso.setNombre(nombre);
        permiso.setDescripcion(descripcion);
        permiso.setModulo(modulo);
        adminServicio.crearPermiso(permiso);
        return "redirect:/admin/permisos";
    }
    
    @GetMapping("/permisos/editar/{id}")
    public String editarPermiso(@PathVariable String id, Model model) {
        Permiso permiso = adminServicio.obtenerPermisoPorId(id);
        model.addAttribute("permiso", permiso);
        return "admin/permiso-form";
    }
    
    @PostMapping("/permisos/actualizar/{id}")
    public String actualizarPermiso(@PathVariable String id,
                                     @RequestParam String nombre,
                                     @RequestParam String descripcion,
                                     @RequestParam String modulo) {
        Permiso permiso = adminServicio.obtenerPermisoPorId(id);
        permiso.setNombre(nombre);
        permiso.setDescripcion(descripcion);
        permiso.setModulo(modulo);
        adminServicio.actualizarPermiso(id, permiso);
        return "redirect:/admin/permisos";
    }
    
    @GetMapping("/permisos/eliminar/{id}")
    public String eliminarPermiso(@PathVariable String id) {
        adminServicio.eliminarPermiso(id);
        return "redirect:/admin/permisos";
    }
    
    // ========== CONFIGURACIONES ==========
    
    @GetMapping("/configuraciones")
    public String configuraciones() {
        return "admin/configuraciones";
    }
    
    @GetMapping("/auditoria")
    public String auditoria() {
        return "admin/auditoria";
    }
}