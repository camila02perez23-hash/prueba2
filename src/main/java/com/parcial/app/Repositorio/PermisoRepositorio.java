// PermisoRepositorio.java
package com.parcial.app.Repositorio;

import com.parcial.app.Modelo.Permiso;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PermisoRepositorio extends MongoRepository<Permiso, String> {
    Optional<Permiso> findByNombre(String nombre);
    List<Permiso> findByModulo(String modulo);
    List<Permiso> findByModuloIn(List<String> modulos);
    boolean existsByNombre(String nombre);
}