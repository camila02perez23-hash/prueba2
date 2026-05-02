// CasoUsoRepositorio.java
package com.parcial.app.Repositorio;

import com.parcial.app.Modelo.CasoUso;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CasoUsoRepositorio extends MongoRepository<CasoUso, String> {
    List<CasoUso> findByProyectoId(String proyectoId);
}