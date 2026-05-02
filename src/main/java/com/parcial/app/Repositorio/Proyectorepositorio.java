package com.parcial.app.Repositorio;

import com.parcial.app.Modelo.Proyecto;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface Proyectorepositorio extends MongoRepository<Proyecto, String> {
    List<Proyecto> findByEstado(String estado);
    List<Proyecto> findByIdProductOwner(String idProductOwner);
    List<Proyecto> findByIdProjectManager(String idProjectManager);
    List<Proyecto> findByNombreContainingIgnoreCase(String nombre);
    
}