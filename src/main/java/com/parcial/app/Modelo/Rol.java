package com.parcial.app.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "roles")
public class Rol {
    
    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private Integer nivelAcceso;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    @DBRef
    private List<Permiso> permisos = new ArrayList<>();
    
    // Constructores
    public Rol() {}
    
    public Rol(String nombre, String descripcion, Integer nivelAcceso) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nivelAcceso = nivelAcceso;
    }
    
    // Getters y Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Integer getNivelAcceso() {
        return nivelAcceso;
    }
    
    public void setNivelAcceso(Integer nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    
    public List<Permiso> getPermisos() {
        return permisos;
    }
    
    public void setPermisos(List<Permiso> permisos) {
        this.permisos = permisos;
    }
}