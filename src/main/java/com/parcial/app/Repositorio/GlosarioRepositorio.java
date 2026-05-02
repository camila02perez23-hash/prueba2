// GlosarioRepositorio.java
package com.parcial.app.Repositorio;

import com.parcial.app.Modelo.GlosarioTermino;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface GlosarioRepositorio extends MongoRepository<GlosarioTermino, String> {
    List<GlosarioTermino> findByProyectoId(String proyectoId);
    Optional<GlosarioTermino> findByTerminoAndProyectoId(String termino, String proyectoId);
}