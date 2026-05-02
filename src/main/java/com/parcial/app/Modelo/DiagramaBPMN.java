package com.parcial.app.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "diagramas_bpmn")
public class DiagramaBPMN {
    @Id
    private String id;
    private String proyectoId;
    private String nombre;
    private String contenido; // XML del diagrama
    private LocalDateTime fechaCreacion;
    
    public DiagramaBPMN() {}
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProyectoId() { return proyectoId; }
    public void setProyectoId(String proyectoId) { this.proyectoId = proyectoId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}