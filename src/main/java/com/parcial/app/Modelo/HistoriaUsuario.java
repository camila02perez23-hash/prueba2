package com.parcial.app.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Document(collection = "historias_usuario")
public class HistoriaUsuario {

    @Id
    private String id;

    private String proyectoId;
    private String idEpica;

    private String titulo;

    private String narrativa;

    private String prioridadMoscow;

    private Integer estimacionPuntos;
    private Integer valorNegocio;

    private String estado = "PENDIENTE";

    private String idSprint;

    private List<CriterioAceptacion> criteriosAceptacion = new ArrayList<>();

    private List<String> comentarios = new ArrayList<>();

    private String observacionAceptacion;

    @CreatedDate
    private LocalDateTime fechaCreacion;

    public static class CriterioAceptacion {
        private String tipo; 
        private String descripcion;
        private int orden;

        public CriterioAceptacion() {}
        public CriterioAceptacion(String tipo, String descripcion, int orden) {
            this.tipo = tipo; this.descripcion = descripcion; this.orden = orden;
        }
        public String getTipo()              { return tipo; }
        public void   setTipo(String tipo)   { this.tipo = tipo; }
        public String getDescripcion()       { return descripcion; }
        public void   setDescripcion(String d){ this.descripcion = d; }
        public int    getOrden()             { return orden; }
        public void   setOrden(int orden)    { this.orden = orden; }
    }

    // ── Constructores ──────────────────────────────────────────
    public HistoriaUsuario() { this.fechaCreacion = LocalDateTime.now(); }

    // ── Getters y Setters ──────────────────────────────────────
    public String getId()                             { return id; }
    public void   setId(String id)                    { this.id = id; }

    public String getIdProyecto()                     { return   proyectoId; }
    public void   setIdProyecto(String idProyecto)    { this.proyectoId = proyectoId; }

    public String getIdEpica()                        { return idEpica; }
    public void   setIdEpica(String idEpica)          { this.idEpica = idEpica; }

    public String getTitulo()                         { return titulo; }
    public void   setTitulo(String titulo)            { this.titulo = titulo; }

    public String getNarrativa()                      { return narrativa; }
    public void   setNarrativa(String narrativa)      { this.narrativa = narrativa; }

    public String getPrioridadMoscow()                { return prioridadMoscow; }
    public void   setPrioridadMoscow(String p)        { this.prioridadMoscow = p; }

    public Integer getEstimacionPuntos()              { return estimacionPuntos; }
    public void    setEstimacionPuntos(Integer e)     { this.estimacionPuntos = e; }

    public Integer getValorNegocio()                  { return valorNegocio; }
    public void    setValorNegocio(Integer v)         { this.valorNegocio = v; }

    public String getEstado()                         { return estado; }
    public void   setEstado(String estado)            { this.estado = estado; }

    public String getIdSprint()                       { return idSprint; }
    public void   setIdSprint(String idSprint)        { this.idSprint = idSprint; }

    public List<CriterioAceptacion> getCriteriosAceptacion()            { return criteriosAceptacion; }
    public void setCreiteriosAceptacion(List<CriterioAceptacion> lista) { this.criteriosAceptacion = lista; }

    public List<String> getComentarios()              { return comentarios; }
    public void         setComentarios(List<String> c){ this.comentarios = c; }

    public String getObservacionAceptacion()          { return observacionAceptacion; }
    public void   setObservacionAceptacion(String o)  { this.observacionAceptacion = o; }

    public LocalDateTime getFechaCreacion()            { return fechaCreacion; }
    public void          setFechaCreacion(LocalDateTime f){ this.fechaCreacion = f; }
}
