package com.parcial.app.Servicio;

import com.parcial.app.Modelo.Tarea;
import com.parcial.app.Repositorio.TareaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TareaServicio {

    @Autowired private TareaRepositorio tareaRepositorio;
    @Autowired private AuditoriaServicio auditoriaServicio;

    public List<Tarea> listarPorProyecto(String idProyecto)      { return tareaRepositorio.findByIdProyecto(idProyecto); }
    public List<Tarea> listarPorAsignado(String idUsuario)       { return tareaRepositorio.findByIdAsignado(idUsuario); }
    public List<Tarea> listarPorHistoria(String idHistoria)      { return tareaRepositorio.findByIdHistoria(idHistoria); }
    public Optional<Tarea> buscarPorId(String id)                { return tareaRepositorio.findById(id); }

    public Tarea crear(Tarea tarea, String correoUsuario, String ip) {
        Tarea guardada = tareaRepositorio.save(tarea);
        auditoriaServicio.registrar(null, correoUsuario, "CREATE", "tareas", guardada.getId(), null, guardada.getTitulo(), ip);
        return guardada;
    }

    public Tarea cambiarEstado(String id, String nuevoEstado, String correoUsuario, String ip) {
        Tarea tarea = tareaRepositorio.findById(id)
            .orElseThrow(() -> new RuntimeException("Tarea no encontrada: " + id));
        String estadoAnterior = tarea.getEstado();
        tarea.setEstado(nuevoEstado);
        Tarea guardada = tareaRepositorio.save(tarea);
        auditoriaServicio.registrar(null, correoUsuario, "UPDATE", "tareas", id, estadoAnterior, nuevoEstado, ip);
        return guardada;
    }

    public Tarea registrarHoras(String id, int horas, String correoUsuario, String ip) {
        Tarea tarea = tareaRepositorio.findById(id)
            .orElseThrow(() -> new RuntimeException("Tarea no encontrada: " + id));
        tarea.setHorasRegistradas((tarea.getHorasRegistradas() == null ? 0 : tarea.getHorasRegistradas()) + horas);
        return tareaRepositorio.save(tarea);
    }
}
