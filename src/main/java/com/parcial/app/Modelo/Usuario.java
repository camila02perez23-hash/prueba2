package com.parcial.app.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "usuarios")
public class Usuario {
    
    @Id
    private String id;
    private String nombres;
    private String apellidos;
    private String correo;
    private String passwordHash;
    private String estado;
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String telefono;
    private String fotoUrl;
    private String rolNombre;  // ← String, no objeto Rol
    
    // Constructores
    public Usuario() {}
    
    // Getters
    public String getId() { return id; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getCorreo() { return correo; }
    public String getPasswordHash() { return passwordHash; }
    public String getEstado() { return estado; }
    public LocalDateTime getFechaIngreso() { return fechaIngreso; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public String getTelefono() { return telefono; }
    public String getFotoUrl() { return fotoUrl; }
    public String getRolNombre() { return rolNombre; }
    
    // Setters
    public void setId(String id) { this.id = id; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setFechaIngreso(LocalDateTime fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
    public void setRolNombre(String rolNombre) { this.rolNombre = rolNombre; }  // ✅ CORRECTO
}