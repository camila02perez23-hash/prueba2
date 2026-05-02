package com.parcial.app.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "casos_uso")
public class CasoUso {
    @Id
    private String id;
    private String proyectoId;
    private String codigo;
    private String nombre;
    private String actor;
    private String flujoBasico;
    private List<String> flujosAlternos;
    private String precondiciones;
    private String postcondiciones;
    private String requisitoId; // Trazabilidad
    private LocalDateTime fechaCreacion;
    
    public CasoUso() {}
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProyectoId() { return proyectoId; }
    public void setProyectoId(String proyectoId) { this.proyectoId = proyectoId; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getActor() { return actor; }
    public void setActor(String actor) { this.actor = actor; }
    public String getFlujoBasico() { return flujoBasico; }
    public void setFlujoBasico(String flujoBasico) { this.flujoBasico = flujoBasico; }
    public List<String> getFlujosAlternos() { return flujosAlternos; }
    public void setFlujosAlternos(List<String> flujosAlternos) { this.flujosAlternos = flujosAlternos; }
    public String getPrecondiciones() { return precondiciones; }
    public void setPrecondiciones(String precondiciones) { this.precondiciones = precondiciones; }
    public String getPostcondiciones() { return postcondiciones; }
    public void setPostcondiciones(String postcondiciones) { this.postcondiciones = postcondiciones; }
    public String getRequisitoId() { return requisitoId; }
    public void setRequisitoId(String requisitoId) { this.requisitoId = requisitoId; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}