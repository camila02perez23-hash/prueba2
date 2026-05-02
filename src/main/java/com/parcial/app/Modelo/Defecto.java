package com.parcial.app.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;


@Document(collection = "defectos")
public class Defecto {

    @Id
    private String id;

    private String idProyecto;
    private String idEjecucion;

    private String titulo;
    private String descripcion;
    private String pasosReproducir;

    private String severidad;

    private String prioridad;

    private String estado = "NUEVO";

    private String ambiente;     
    private String evidenciaUrl; 
    private String navegador;

    private String idReportadoPor;
    private String idAsignadoA;

    private String versionAfectada;

    @CreatedDate
    private LocalDateTime fechaReporte;
    private LocalDateTime fechaResolucion;

    public Defecto() { this.fechaReporte = LocalDateTime.now(); }

    public String getId()                        { return id; }
    public void   setId(String id)               { this.id = id; }
    public String getIdProyecto()                { return idProyecto; }
    public void   setIdProyecto(String v)        { this.idProyecto = v; }
    public String getIdEjecucion()               { return idEjecucion; }
    public void   setIdEjecucion(String v)       { this.idEjecucion = v; }
    public String getTitulo()                    { return titulo; }
    public void   setTitulo(String v)            { this.titulo = v; }
    public String getDescripcion()               { return descripcion; }
    public void   setDescripcion(String v)       { this.descripcion = v; }
    public String getPasosReproducir()           { return pasosReproducir; }
    public void   setPasosReproducir(String v)   { this.pasosReproducir = v; }
    public String getSeveridad()                 { return severidad; }
    public void   setSeveridad(String v)         { this.severidad = v; }
    public String getPrioridad()                 { return prioridad; }
    public void   setPrioridad(String v)         { this.prioridad = v; }
    public String getEstado()                    { return estado; }
    public void   setEstado(String v)            { this.estado = v; }
    public String getAmbiente()                  { return ambiente; }
    public void   setAmbiente(String v)          { this.ambiente = v; }
    public String getEvidenciaUrl()              { return evidenciaUrl; }
    public void   setEvidenciaUrl(String v)      { this.evidenciaUrl = v; }
    public String getNavegador()                 { return navegador; }
    public void   setNavegador(String v)         { this.navegador = v; }
    public String getIdReportadoPor()            { return idReportadoPor; }
    public void   setIdReportadoPor(String v)    { this.idReportadoPor = v; }
    public String getIdAsignadoA()               { return idAsignadoA; }
    public void   setIdAsignadoA(String v)       { this.idAsignadoA = v; }
    public String getVersionAfectada()           { return versionAfectada; }
    public void   setVersionAfectada(String v)   { this.versionAfectada = v; }
    public LocalDateTime getFechaReporte()       { return fechaReporte; }
    public void setFechaReporte(LocalDateTime v) { this.fechaReporte = v; }
    public LocalDateTime getFechaResolucion()    { return fechaResolucion; }
    public void setFechaResolucion(LocalDateTime v){ this.fechaResolucion = v; }
}
