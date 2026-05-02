package com.parcial.app.Servicio;

import com.parcial.app.Modelo.Permiso;
import com.parcial.app.Modelo.Rol;
import com.parcial.app.Repositorio.PermisoRepositorio;
import com.parcial.app.Repositorio.RolRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminServicio {
    
    @Autowired
    private RolRepositorio rolRepositorio;
    
    @Autowired
    private PermisoRepositorio permisoRepositorio;
    
    // ========== ROLES ==========
    
    public List<Rol> listarRoles() {
        return rolRepositorio.findAll();
    }
    
    public Rol obtenerRolPorId(String id) {
        return rolRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + id));
    }
    
    public Rol crearRol(Rol rol) {
        return rolRepositorio.save(rol);
    }
    
    public Rol actualizarRol(String id, Rol rol) {
        Rol existente = obtenerRolPorId(id);
        existente.setNombre(rol.getNombre());
        existente.setDescripcion(rol.getDescripcion());
        existente.setNivelAcceso(rol.getNivelAcceso());
        return rolRepositorio.save(existente);
    }
    
    public void eliminarRol(String id) {
        rolRepositorio.deleteById(id);
    }
    
    public long contarRoles() {  // ✅ NUEVO MÉTODO
        return rolRepositorio.count();
    }
    
    public Rol asignarPermisoARol(String rolId, String permisoId) {
        Rol rol = obtenerRolPorId(rolId);
        Permiso permiso = obtenerPermisoPorId(permisoId);
        rol.getPermisos().add(permiso);
        return rolRepositorio.save(rol);
    }
    
    public Rol removerPermisoDeRol(String rolId, String permisoId) {
        Rol rol = obtenerRolPorId(rolId);
        Permiso permiso = obtenerPermisoPorId(permisoId);
        rol.getPermisos().remove(permiso);
        return rolRepositorio.save(rol);
    }
    
    // ========== PERMISOS ==========
    
    public List<Permiso> listarPermisos() {
        return permisoRepositorio.findAll();
    }
    
    public Permiso obtenerPermisoPorId(String id) {
        return permisoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado: " + id));
    }
    
    public Permiso crearPermiso(Permiso permiso) {
        return permisoRepositorio.save(permiso);
    }
    
    public Permiso actualizarPermiso(String id, Permiso permiso) {
        Permiso existente = obtenerPermisoPorId(id);
        existente.setNombre(permiso.getNombre());
        existente.setDescripcion(permiso.getDescripcion());
        existente.setModulo(permiso.getModulo());
        return permisoRepositorio.save(existente);
    }
    
    public void eliminarPermiso(String id) {
        permisoRepositorio.deleteById(id);
    }
    
    public long contarPermisos() {  // ✅ NUEVO MÉTODO
        return permisoRepositorio.count();
    }
    
    public List<Permiso> listarPermisosPorModulo(String modulo) {
        return permisoRepositorio.findByModulo(modulo);
    }
    
    
}