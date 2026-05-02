package com.parcial.app.Repositorio;

import com.parcial.app.Modelo.HistoriaUsuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface HistoriaUsuarioRepositorio extends MongoRepository<HistoriaUsuario, String> {
    List<HistoriaUsuario> findByIdProyecto(String idProyecto);
    List<HistoriaUsuario> findByIdSprint(String idSprint);
    List<HistoriaUsuario> findByProyectoId(String proyectoId);  
    List<HistoriaUsuario> findByIdProyectoAndEstado(String idProyecto, String estado);
    List<HistoriaUsuario> findByIdEpica(String idEpica);
    List<HistoriaUsuario> findByIdProyectoAndIdSprintIsNull(String idProyecto);
}