package com.parcial.app.Repositorio;
import com.parcial.app.Modelo.Bitacoraauditoria;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
public interface BitacoraAuditoriaRepositorio extends MongoRepository<Bitacoraauditoria, String> {
    List<Bitacoraauditoria> findByIdUsuarioOrderByFechaDesc(String idUsuario);
    List<Bitacoraauditoria> findByEntidadAndEntidadId(String entidad, String entidadId);
    List<Bitacoraauditoria> findTop50ByOrderByFechaDesc();
}
