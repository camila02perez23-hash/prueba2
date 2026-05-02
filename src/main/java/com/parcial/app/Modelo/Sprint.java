package com.parcial.app.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Document(collection = "sprints")
public class Sprint {

    @Id
    private String id;

    private String idProyecto;
    private Integer numero;
    private String objetivo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer capacidadHoras;

    private String estado = "PLANIFICADO";

    @CreatedDate
    private LocalDateTime fechaCreacion;

    public Sprint() { this.fechaCreacion = LocalDateTime.now(); }

    public Sprint(String idProyecto, Integer numero, String objetivo,
                  LocalDate fechaInicio, LocalDate fechaFin, Integer capacidadHoras) {
        this(); this.idProyecto = idProyecto; this.numero = numero;
        this.objetivo = objetivo; this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin; this.capacidadHoras = capacidadHoras;
    }

    public String  getId()                              { return id; }
    public void    setId(String id)                     { this.id = id; }
    public String  getIdProyecto()                      { return idProyecto; }
    public void    setIdProyecto(String v)              { this.idProyecto = v; }
    public Integer getNumero()                          { return numero; }
    public void    setNumero(Integer v)                 { this.numero = v; }
    public String  getObjetivo()                        { return objetivo; }
    public void    setObjetivo(String v)                { this.objetivo = v; }
    public LocalDate getFechaInicio()                   { return fechaInicio; }
    public void      setFechaInicio(LocalDate v)        { this.fechaInicio = v; }
    public LocalDate getFechaFin()                      { return fechaFin; }
    public void      setFechaFin(LocalDate v)           { this.fechaFin = v; }
    public Integer getCapacidadHoras()                  { return capacidadHoras; }
    public void    setCapacidadHoras(Integer v)         { this.capacidadHoras = v; }
    public String  getEstado()                          { return estado; }
    public void    setEstado(String v)                  { this.estado = v; }
    public LocalDateTime getFechaCreacion()             { return fechaCreacion; }
    public void          setFechaCreacion(LocalDateTime v){ this.fechaCreacion = v; }
}