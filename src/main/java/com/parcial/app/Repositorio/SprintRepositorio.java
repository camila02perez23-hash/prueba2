package com.parcial.app.Repositorio;
import com.parcial.app.Modelo.Sprint;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;
public interface SprintRepositorio extends MongoRepository<Sprint, String> {
    List<Sprint>    findByIdProyecto(String idProyecto);
    Optional<Sprint> findByIdProyectoAndEstado(String idProyecto, String estado);
    List<Sprint>    findByIdProyectoOrderByNumeroAsc(String idProyecto);
}