// DiagramaBPMNRepositorio.java
package com.parcial.app.Repositorio;

import com.parcial.app.Modelo.DiagramaBPMN;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface DiagramaBPMNRepositorio extends MongoRepository<DiagramaBPMN, String> {
    List<DiagramaBPMN> findByProyectoId(String proyectoId);
}