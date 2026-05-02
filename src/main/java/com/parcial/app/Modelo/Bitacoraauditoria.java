package com.parcial.app.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;


@Document(collection = "bitacora_auditoria")
public class Bitacoraauditoria {

    @Id
    private String id;

    private String idUsuario;
    private String correoUsuario;

    private String accion;

    private String entidad;
    private String entidadId;

    private String datosAnteriores;  
    private String datosNuevos;   

    private LocalDateTime fecha;
    private String ip;

    public Bitacoraauditoria() { this.fecha = LocalDateTime.now(); }

    public Bitacoraauditoria(String idUsuario, String correoUsuario,
                              String accion, String entidad, String entidadId,
                              String datosAnteriores, String datosNuevos, String ip) {
        this();
        this.idUsuario       = idUsuario;
        this.correoUsuario   = correoUsuario;
        this.accion          = accion;
        this.entidad         = entidad;
        this.entidadId       = entidadId;
        this.datosAnteriores = datosAnteriores;
        this.datosNuevos     = datosNuevos;
        this.ip              = ip;
    }

    public String getId()                          { return id; }
    public void   setId(String id)                 { this.id = id; }
    public String getIdUsuario()                   { return idUsuario; }
    public void   setIdUsuario(String v)           { this.idUsuario = v; }
    public String getCorreoUsuario()               { return correoUsuario; }
    public void   setCorreoUsuario(String v)       { this.correoUsuario = v; }
    public String getAccion()                      { return accion; }
    public void   setAccion(String v)              { this.accion = v; }
    public String getEntidad()                     { return entidad; }
    public void   setEntidad(String v)             { this.entidad = v; }
    public String getEntidadId()                   { return entidadId; }
    public void   setEntidadId(String v)           { this.entidadId = v; }
    public String getDatosAnteriores()             { return datosAnteriores; }
    public void   setDatosAnteriores(String v)     { this.datosAnteriores = v; }
    public String getDatosNuevos()                 { return datosNuevos; }
    public void   setDatosNuevos(String v)         { this.datosNuevos = v; }
    public LocalDateTime getFecha()                { return fecha; }
    public void          setFecha(LocalDateTime v) { this.fecha = v; }
    public String getIp()                          { return ip; }
    public void   setIp(String v)                  { this.ip = v; }
}