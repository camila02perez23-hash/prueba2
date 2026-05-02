package com.parcial.app.Servicio;

import com.parcial.app.Modelo.Usuario;
import com.parcial.app.Repositorio.UsuarioRepositorio;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServicio {
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    public List<Usuario> listarTodos() {
        return usuarioRepositorio.findAll();
    }
    
    public Optional<Usuario> obtenerPorId(String id) {
        try {
            // Convertir String a ObjectId
            ObjectId objectId = new ObjectId(id);
            return usuarioRepositorio.findById(objectId.toString());
        } catch (IllegalArgumentException e) {
            System.out.println("ID inválido: " + id);
            return Optional.empty();
        }
    }
    
    public Usuario guardar(Usuario usuario) {
        return usuarioRepositorio.save(usuario);
    }
    
    public void eliminar(String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            usuarioRepositorio.deleteById(objectId.toString());
        } catch (IllegalArgumentException e) {
            System.out.println("ID inválido para eliminar: " + id);
        }
    }
}