// EntrevistaRepositorio.java
package com.parcial.app.Repositorio;

import com.parcial.app.Modelo.Entrevista;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EntrevistaRepositorio extends MongoRepository<Entrevista, String> {}