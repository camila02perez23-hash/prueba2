package com.parcial.app.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "validaciones")
public class ValidacionRequisito {
    @Id
    private String id;
    private String requisitoId;
    private String clienteId;
    private String estado; // PENDIENTE, APROBADO, RECHAZADO
    private String observacion;
    private String firmaElectronica;
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaRespuesta;
    
    public ValidacionRequisito() {}
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRequisitoId() { return requisitoId; }
    public void setRequisitoId(String requisitoId) { this.requisitoId = requisitoId; }
    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public String getFirmaElectronica() { return firmaElectronica; }
    public void setFirmaElectronica(String firmaElectronica) { this.firmaElectronica = firmaElectronica; }
    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
    public LocalDateTime getFechaRespuesta() { return fechaRespuesta; }
    public void setFechaRespuesta(LocalDateTime fechaRespuesta) { this.fechaRespuesta = fechaRespuesta; }
}