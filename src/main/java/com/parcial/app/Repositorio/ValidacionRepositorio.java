// ValidacionRepositorio.java
package com.parcial.app.Repositorio;

import com.parcial.app.Modelo.ValidacionRequisito;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ValidacionRepositorio extends MongoRepository<ValidacionRequisito, String> {
    List<ValidacionRequisito> findByRequisitoId(String requisitoId);
    List<ValidacionRequisito> findByEstado(String estado);
}