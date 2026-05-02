package com.parcial.app.Repositorio;
import com.parcial.app.Modelo.Tarea;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
public interface TareaRepositorio extends MongoRepository<Tarea, String> {
    List<Tarea> findByIdProyecto(String idProyecto);
    List<Tarea> findByIdHistoria(String idHistoria);
    List<Tarea> findByIdAsignado(String idAsignado);
    List<Tarea> findByIdAsignadoAndEstado(String idAsignado, String estado);
    List<Tarea> findByIdProyectoAndTipo(String idProyecto, String tipo);
    long countByIdProyectoAndEstado(String idProyecto, String estado);
}
