package com.parcial.app.DTO;

public class UsuarioRegistroDTO {
    private String nombre;
    private String correo;
    private String password;
    private String rol;  // ← Esto es String, no Rol
    
    // Constructor vacío
    public UsuarioRegistroDTO() {}
    
    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}