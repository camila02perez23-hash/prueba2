package com.parcial.app.Repositorio;
import com.parcial.app.Modelo.Defecto;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
public interface DefectoRepositorio extends MongoRepository<Defecto, String> {
    List<Defecto> findByIdProyecto(String idProyecto);
    List<Defecto> findByIdAsignadoA(String idAsignadoA);
    List<Defecto> findByIdProyectoAndEstado(String idProyecto, String estado);
    List<Defecto> findByIdProyectoAndSeveridad(String idProyecto, String severidad);
    long countByIdProyectoAndEstadoNot(String idProyecto, String estado);
}
