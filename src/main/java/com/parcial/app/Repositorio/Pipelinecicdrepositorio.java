package com.parcial.app.Repositorio;
import com.parcial.app.Modelo.Pipelinecicd;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
public interface Pipelinecicdrepositorio extends MongoRepository<Pipelinecicd, String> {
    List<Pipelinecicd> findByIdProyecto(String idProyecto);
    List<Pipelinecicd> findByEstadoUltimaEjecucion(String estado);
}
