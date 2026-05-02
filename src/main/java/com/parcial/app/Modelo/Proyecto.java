package com.parcial.app.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Document(collection = "proyectos")
public class Proyecto {

    @Id
    private String id;

    private String nombre;
    private String descripcion;

    private String idCliente;
    private String nombreCliente; 

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    private Double presupuesto;

    private String metodologia;

    private String estado = "ACTIVO";

    private String idProductOwner;
    private String idProjectManager;

    @CreatedDate
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    private LocalDateTime fechaActualizacion;

    // ── Constructores ──────────────────────────────────────────
    public Proyecto() {}

    public Proyecto(String nombre, String descripcion, String idCliente,
                    String nombreCliente, LocalDate fechaInicio, LocalDate fechaFin,
                    Double presupuesto, String metodologia) {
        this.nombre         = nombre;
        this.descripcion    = descripcion;
        this.idCliente      = idCliente;
        this.nombreCliente  = nombreCliente;
        this.fechaInicio    = fechaInicio;
        this.fechaFin       = fechaFin;
        this.presupuesto    = presupuesto;
        this.metodologia    = metodologia;
        this.estado         = "ACTIVO";
        this.fechaCreacion  = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    // ── Getters y Setters ──────────────────────────────────────
    public String getId()                        { return id; }
    public void   setId(String id)               { this.id = id; }

    public String getNombre()                    { return nombre; }
    public void   setNombre(String nombre)       { this.nombre = nombre; }

    public String getDescripcion()               { return descripcion; }
    public void   setDescripcion(String d)       { this.descripcion = d; }

    public String getIdCliente()                 { return idCliente; }
    public void   setIdCliente(String idCliente) { this.idCliente = idCliente; }

    public String getNombreCliente()                       { return nombreCliente; }
    public void   setNombreCliente(String nombreCliente)   { this.nombreCliente = nombreCliente; }

    public LocalDate getFechaInicio()                      { return fechaInicio; }
    public void      setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin()                         { return fechaFin; }
    public void      setFechaFin(LocalDate fechaFin)       { this.fechaFin = fechaFin; }

    public Double getPresupuesto()                         { return presupuesto; }
    public void   setPresupuesto(Double presupuesto)       { this.presupuesto = presupuesto; }

    public String getMetodologia()                         { return metodologia; }
    public void   setMetodologia(String metodologia)       { this.metodologia = metodologia; }

    public String getEstado()                              { return estado; }
    public void   setEstado(String estado)                 { this.estado = estado; }

    public String getIdProductOwner()                      { return idProductOwner; }
    public void   setIdProductOwner(String id)             { this.idProductOwner = id; }

    public String getIdProjectManager()                    { return idProjectManager; }
    public void   setIdProjectManager(String id)           { this.idProjectManager = id; }

    public LocalDateTime getFechaCreacion()                        { return fechaCreacion; }
    public void           setFechaCreacion(LocalDateTime f)        { this.fechaCreacion = f; }

    public LocalDateTime getFechaActualizacion()                   { return fechaActualizacion; }
    public void           setFechaActualizacion(LocalDateTime f)   { this.fechaActualizacion = f; }
}