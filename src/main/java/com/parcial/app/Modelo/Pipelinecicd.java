package com.parcial.app.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;


@Document(collection = "pipelines")
public class Pipelinecicd {

    @Id
    private String id;

    private String idProyecto;
    private String nombre;
    private String repositorioUrl;
    private String rama;

    private String etapasJson;

    /** EXITOSO, FALLIDO, EN_EJECUCION, PENDIENTE */
    private String estadoUltimaEjecucion = "PENDIENTE";

    private LocalDateTime fechaUltimaEjecucion;

    @CreatedDate
    private LocalDateTime fechaCreacion;

    public Pipelinecicd() { this.fechaCreacion = LocalDateTime.now(); }

    public String getId()                            { return id; }
    public void   setId(String id)                   { this.id = id; }
    public String getIdProyecto()                    { return idProyecto; }
    public void   setIdProyecto(String v)            { this.idProyecto = v; }
    public String getNombre()                        { return nombre; }
    public void   setNombre(String v)                { this.nombre = v; }
    public String getRepositorioUrl()                { return repositorioUrl; }
    public void   setRepositorioUrl(String v)        { this.repositorioUrl = v; }
    public String getRama()                          { return rama; }
    public void   setRama(String v)                  { this.rama = v; }
    public String getEtapasJson()                    { return etapasJson; }
    public void   setEtapasJson(String v)            { this.etapasJson = v; }
    public String getEstadoUltimaEjecucion()         { return estadoUltimaEjecucion; }
    public void   setEstadoUltimaEjecucion(String v) { this.estadoUltimaEjecucion = v; }
    public LocalDateTime getFechaUltimaEjecucion()   { return fechaUltimaEjecucion; }
    public void setFechaUltimaEjecucion(LocalDateTime v){ this.fechaUltimaEjecucion = v; }
    public LocalDateTime getFechaCreacion()          { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime v)    { this.fechaCreacion = v; }
}