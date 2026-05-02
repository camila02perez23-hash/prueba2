// RolRepositorio.java
package com.parcial.app.Repositorio;

import com.parcial.app.Modelo.Rol;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolRepositorio extends MongoRepository<Rol, String> {
    Optional<Rol> findByNombre(String nombre);
    List<Rol> findByNivelAccesoLessThanEqual(Integer nivel);
    boolean existsByNombre(String nombre);
}