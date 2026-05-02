package com.parcial.app.Servicio;

import com.parcial.app.Modelo.Defecto;
import com.parcial.app.Repositorio.DefectoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class DefectoServicio {

    @Autowired private DefectoRepositorio defectoRepositorio;
    @Autowired private AuditoriaServicio  auditoriaServicio;

    public List<Defecto> listarPorProyecto(String idProyecto)    { return defectoRepositorio.findByIdProyecto(idProyecto); }
    public List<Defecto> listarPorAsignado(String idUsuario)     { return defectoRepositorio.findByIdAsignadoA(idUsuario); }
    public Optional<Defecto> buscarPorId(String id)              { return defectoRepositorio.findById(id); }

    public Defecto reportar(Defecto defecto, String correoUsuario, String ip) {
        defecto.setEstado("NUEVO");
        defecto.setFechaReporte(LocalDateTime.now());
        Defecto guardado = defectoRepositorio.save(defecto);
        auditoriaServicio.registrar(null, correoUsuario, "CREATE", "defectos", guardado.getId(), null, guardado.getTitulo(), ip);
        return guardado;
    }

    public Defecto cambiarEstado(String id, String nuevoEstado, String correoUsuario, String ip) {
        Defecto defecto = defectoRepositorio.findById(id)
            .orElseThrow(() -> new RuntimeException("Defecto no encontrado: " + id));
        String estadoAnterior = defecto.getEstado();

        if ("RESUELTO".equals(nuevoEstado) || "CERRADO".equals(nuevoEstado)) {
            defecto.setFechaResolucion(LocalDateTime.now());
        }

        defecto.setEstado(nuevoEstado);
        Defecto guardado = defectoRepositorio.save(defecto);
        auditoriaServicio.registrar(null, correoUsuario, "UPDATE", "defectos", id, estadoAnterior, nuevoEstado, ip);
        return guardado;
    }

    public long contarAbiertos(String idProyecto) {
        return defectoRepositorio.countByIdProyectoAndEstadoNot(idProyecto, "CERRADO");
    }
}
