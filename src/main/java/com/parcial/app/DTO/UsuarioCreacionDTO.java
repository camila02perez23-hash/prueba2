package com.parcial.app.DTO;

public class UsuarioCreacionDTO {
    private String nombres;
    private String apellidos;
    private String correo;
    private String password;
    private String rolNombre;
    
    public UsuarioCreacionDTO() {}
    
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRolNombre() { return rolNombre; }
    public void setRolNombre(String rolNombre) { this.rolNombre = rolNombre; }
}