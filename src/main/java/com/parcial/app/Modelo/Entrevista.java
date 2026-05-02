package com.parcial.app.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "entrevistas")
public class Entrevista {
    @Id
    private String id;
    private String titulo;
    private String tecnica; // Entrevista, Taller JAD, Brainstorming
    private List<String> participantes;
    private String transcripcion;
    private LocalDateTime fecha;
    
    public Entrevista() {}
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getTecnica() { return tecnica; }
    public void setTecnica(String tecnica) { this.tecnica = tecnica; }
    public List<String> getParticipantes() { return participantes; }
    public void setParticipantes(List<String> participantes) { this.participantes = participantes; }
    public String getTranscripcion() { return transcripcion; }
    public void setTranscripcion(String transcripcion) { this.transcripcion = transcripcion; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}