package com.parcial.app.Servicio;

import com.parcial.app.Modelo.Proyecto;
import com.parcial.app.Repositorio.Proyectorepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProyectoServicio {

    @Autowired
    private Proyectorepositorio proyectoRepositorio;

    @Autowired
    private AuditoriaServicio auditoriaServicio;

    public List<Proyecto> listarTodos()                        { return proyectoRepositorio.findAll(); }
    public List<Proyecto> listarActivos()                      { return proyectoRepositorio.findByEstado("ACTIVO"); }
    public Optional<Proyecto> buscarPorId(String id)           { return proyectoRepositorio.findById(id); }

    public Proyecto crear(Proyecto proyecto, String correoUsuario, String ip) {
        proyecto.setFechaCreacion(LocalDateTime.now());
        proyecto.setFechaActualizacion(LocalDateTime.now());
        Proyecto guardado = proyectoRepositorio.save(proyecto);
        auditoriaServicio.registrar(null, correoUsuario, "CREATE",
            "proyectos", guardado.getId(), null, guardado.getNombre(), ip);
        return guardado;
    }

    public Proyecto actualizar(Proyecto proyecto, String correoUsuario, String ip) {
        proyecto.setFechaActualizacion(LocalDateTime.now());
        Proyecto guardado = proyectoRepositorio.save(proyecto);
        auditoriaServicio.registrar(null, correoUsuario, "UPDATE",
            "proyectos", guardado.getId(), null, guardado.getNombre(), ip);
        return guardado;
    }

    public void eliminar(String id, String correoUsuario, String ip) {
        auditoriaServicio.registrar(null, correoUsuario, "DELETE",
            "proyectos", id, id, null, ip);
        proyectoRepositorio.deleteById(id);
    }

    public long contarActivos() { return proyectoRepositorio.findByEstado("ACTIVO").size(); }
}
