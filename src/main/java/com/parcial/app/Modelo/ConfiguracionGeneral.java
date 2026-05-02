package com.parcial.app.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "configuracion")
public class ConfiguracionGeneral {
    @Id
    private String id;
    private String nombreEmpresa = "TechSoft Solutions S.A.S.";
    private String logoUrl = "";
    private String telefonoContacto = "";
    private String emailContacto = "contacto@techsoft.com";
    private String direccion = "Bucaramanga, Colombia";
    private Integer tiempoSesionMinutos = 30;
    private Boolean registrarAuditoria = true;

    public ConfiguracionGeneral() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombreEmpresa() { return nombreEmpresa; }
    public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public String getTelefonoContacto() { return telefonoContacto; }
    public void setTelefonoContacto(String telefonoContacto) { this.telefonoContacto = telefonoContacto; }
    public String getEmailContacto() { return emailContacto; }
    public void setEmailContacto(String emailContacto) { this.emailContacto = emailContacto; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public Integer getTiempoSesionMinutos() { return tiempoSesionMinutos; }
    public void setTiempoSesionMinutos(Integer tiempoSesionMinutos) { this.tiempoSesionMinutos = tiempoSesionMinutos; }
    public Boolean getRegistrarAuditoria() { return registrarAuditoria; }
    public void setRegistrarAuditoria(Boolean registrarAuditoria) { this.registrarAuditoria = registrarAuditoria; }
}