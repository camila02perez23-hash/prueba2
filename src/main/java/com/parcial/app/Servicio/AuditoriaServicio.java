package com.parcial.app.Servicio;

import com.parcial.app.Modelo.Bitacoraauditoria;
import com.parcial.app.Repositorio.BitacoraAuditoriaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuditoriaServicio {

    @Autowired
    private BitacoraAuditoriaRepositorio bitacoraRepositorio;

    public void registrar(String idUsuario, String correoUsuario,
                          String accion, String entidad, String entidadId,
                          String datosAnteriores, String datosNuevos, String ip) {
        Bitacoraauditoria registro = new Bitacoraauditoria(
            idUsuario, correoUsuario, accion, entidad, entidadId,
            datosAnteriores, datosNuevos, ip
        );
        bitacoraRepositorio.save(registro);
    }

    public void registrar(String idUsuario, String correoUsuario,
                          String accion, String entidad, String ip) {
        registrar(idUsuario, correoUsuario, accion, entidad, null, null, null, ip);
    }
}
