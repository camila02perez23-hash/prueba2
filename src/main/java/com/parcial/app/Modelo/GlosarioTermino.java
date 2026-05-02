package com.parcial.app.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "glosario")
public class GlosarioTermino {
    @Id
    private String id;
    private String proyectoId;
    private String termino;
    private String definicion;
    private String categoria;
    private String contexto;
    private LocalDateTime fechaCreacion;
    
    public GlosarioTermino() {}
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProyectoId() { return proyectoId; }
    public void setProyectoId(String proyectoId) { this.proyectoId = proyectoId; }
    public String getTermino() { return termino; }
    public void setTermino(String termino) { this.termino = termino; }
    public String getDefinicion() { return definicion; }
    public void setDefinicion(String definicion) { this.definicion = definicion; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getContexto() { return contexto; }
    public void setContexto(String contexto) { this.contexto = contexto; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}