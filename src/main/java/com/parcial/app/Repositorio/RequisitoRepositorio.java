// RequisitoRepositorio.java
package com.parcial.app.Repositorio;

import com.parcial.app.Modelo.Requisito;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface RequisitoRepositorio extends MongoRepository<Requisito, String> {
    List<Requisito> findByProyectoId(String proyectoId);
    List<Requisito> findByEstado(String estado);
}