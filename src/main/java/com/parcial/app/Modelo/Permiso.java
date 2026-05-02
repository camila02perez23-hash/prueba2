package com.parcial.app.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "permisos")
public class Permiso {
    
    @Id
    private String id;
    private String nombre;          // ej: "CREAR_PROYECTO", "APROBAR_ENTREGABLE"
    private String descripcion;
    private String modulo;          // ej: "ADMIN", "PROYECTOS", "BACKLOG", "PRUEBAS"
    
    // Constructores
    public Permiso() {}
    
    public Permiso(String nombre, String descripcion, String modulo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.modulo = modulo;
    }
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public String getModulo() { return modulo; }
    public void setModulo(String modulo) { this.modulo = modulo; }
    
    @Override
    public String toString() {
        return "Permiso{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", modulo='" + modulo + '\'' +
                '}';
    }
}