package com.parcial.app.Repositorio;

import com.parcial.app.Modelo.ConfiguracionGeneral;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracionRepositorio extends MongoRepository<ConfiguracionGeneral, String> {
}