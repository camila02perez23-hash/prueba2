// UsuarioRepositorio.java
package com.parcial.app.Repositorio;

import com.parcial.app.Modelo.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UsuarioRepositorio extends MongoRepository<Usuario, String> {
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}