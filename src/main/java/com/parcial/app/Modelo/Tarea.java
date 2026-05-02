package com.parcial.app.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Document(collection = "tareas")
public class Tarea {

    @Id
    private String id;

    private String idHistoria;
    private String idProyecto;
    private String titulo;
    private String descripcion;

    private String tipo;

    private String idAsignado;      
    private String nombreAsignado;   

    private Integer horasEstimadas;
    private Integer horasReales;
    private LocalDate fechaLimite;

    private String estado = "PENDIENTE";

    private String ramaGit;
    private String urlPullRequest;

    private Integer horasRegistradas = 0;

    @CreatedDate
    private LocalDateTime fechaCreacion;

    public Tarea() { this.fechaCreacion = LocalDateTime.now(); }

    // ── Getters y Setters ──────────────────────────────────────
    public String  getId()                    { return id; }
    public void    setId(String id)           { this.id = id; }
    public String  getIdHistoria()            { return idHistoria; }
    public void    setIdHistoria(String v)    { this.idHistoria = v; }
    public String  getIdProyecto()            { return idProyecto; }
    public void    setIdProyecto(String v)    { this.idProyecto = v; }
    public String  getTitulo()                { return titulo; }
    public void    setTitulo(String v)        { this.titulo = v; }
    public String  getDescripcion()           { return descripcion; }
    public void    setDescripcion(String v)   { this.descripcion = v; }
    public String  getTipo()                  { return tipo; }
    public void    setTipo(String v)          { this.tipo = v; }
    public String  getIdAsignado()            { return idAsignado; }
    public void    setIdAsignado(String v)    { this.idAsignado = v; }
    public String  getNombreAsignado()        { return nombreAsignado; }
    public void    setNombreAsignado(String v){ this.nombreAsignado = v; }
    public Integer getHorasEstimadas()        { return horasEstimadas; }
    public void    setHorasEstimadas(Integer v){ this.horasEstimadas = v; }
    public Integer getHorasReales()           { return horasReales; }
    public void    setHorasReales(Integer v)  { this.horasReales = v; }
    public LocalDate getFechaLimite()         { return fechaLimite; }
    public void      setFechaLimite(LocalDate v){ this.fechaLimite = v; }
    public String  getEstado()                { return estado; }
    public void    setEstado(String v)        { this.estado = v; }
    public String  getRamaGit()               { return ramaGit; }
    public void    setRamaGit(String v)       { this.ramaGit = v; }
    public String  getUrlPullRequest()        { return urlPullRequest; }
    public void    setUrlPullRequest(String v){ this.urlPullRequest = v; }
    public Integer getHorasRegistradas()      { return horasRegistradas; }
    public void    setHorasRegistradas(Integer v){ this.horasRegistradas = v; }
    public LocalDateTime getFechaCreacion()   { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime v){ this.fechaCreacion = v; }
}