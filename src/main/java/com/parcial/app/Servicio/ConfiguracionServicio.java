package com.parcial.app.Servicio;

import com.parcial.app.Modelo.ConfiguracionGeneral;
import com.parcial.app.Repositorio.ConfiguracionRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class ConfiguracionServicio {

    @Autowired
    private ConfiguracionRepositorio configuracionRepositorio;

    public void inicializarConfiguracion() {
        if (configuracionRepositorio.count() == 0) {
            configuracionRepositorio.save(new ConfiguracionGeneral());
            System.out.println("✅ Configuración inicial creada");
        }
    }

    public ConfiguracionGeneral obtenerConfiguracion() {
        return configuracionRepositorio.findAll().iterator().next();
    }

    public ConfiguracionGeneral guardarConfiguracion(ConfiguracionGeneral config) {
        return configuracionRepositorio.save(config);
    }
}