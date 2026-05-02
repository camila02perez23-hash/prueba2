package com.parcial.app.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "requisitos")
public class Requisito {
    @Id
    private String id;
    private String proyectoId;
    private String codigo;
    private String tipo; // FUNCIONAL / NO_FUNCIONAL
    private String nombre;
    private String descripcion;
    private String prioridad; // ALTA, MEDIA, BAJA
    private String estado; // PENDIENTE, APROBADO, RECHAZADO, EN_VALIDACION
    private Integer version;
    private String analistaId;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public Requisito() {}
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProyectoId() { return proyectoId; }
    public void setProyectoId(String proyectoId) { this.proyectoId = proyectoId; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getAnalistaId() { return analistaId; }
    public void setAnalistaId(String analistaId) { this.analistaId = analistaId; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}