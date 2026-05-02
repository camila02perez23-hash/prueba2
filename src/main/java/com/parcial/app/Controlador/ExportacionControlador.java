package com.parcial.app.Controlador;

import com.itextpdf.text.DocumentException;
import com.parcial.app.Modelo.*;
import com.parcial.app.Repositorio.*;
import com.parcial.app.Servicio.ExportacionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/exportar")
public class ExportacionControlador {
    
    @Autowired
    private ExportacionServicio exportacionServicio;
    
    @Autowired
    private Proyectorepositorio proyectoRepositorio;
    
    @Autowired
    private RequisitoRepositorio requisitoRepositorio;
    
    @Autowired
    private CasoUsoRepositorio casoUsoRepositorio;
    
    @Autowired
    private HistoriaUsuarioRepositorio historiaRepositorio;
    
    @Autowired
    private GlosarioRepositorio glosarioRepositorio;
    
    // ========== EXPORTAR PROYECTOS A PDF ==========
    @GetMapping("/proyectos/pdf")
    public ResponseEntity<byte[]> exportarProyectosPDF() throws DocumentException {
        List<Proyecto> proyectos = proyectoRepositorio.findAll();
        byte[] pdf = exportacionServicio.exportarProyectosAPDF(proyectos);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=proyectos.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
    
    // ========== EXPORTAR REQUISITOS A WORD ==========
    @GetMapping("/requisitos/word/{proyectoId}")
    public ResponseEntity<byte[]> exportarRequisitosWord(@PathVariable String proyectoId) throws IOException {
        List<Requisito> requisitos = requisitoRepositorio.findByProyectoId(proyectoId);
        String filename = "requisitos_" + proyectoId + ".docx";
        byte[] word = exportacionServicio.exportarRequisitosAWord(requisitos);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(word);
    }
    
    // ========== EXPORTAR MATRIZ DE TRAZABILIDAD A PDF ==========
    @GetMapping("/trazabilidad/pdf/{proyectoId}")
    public ResponseEntity<byte[]> exportarTrazabilidadPDF(@PathVariable String proyectoId) throws DocumentException {
        List<Requisito> requisitos = requisitoRepositorio.findByProyectoId(proyectoId);
        List<CasoUso> casosUso = casoUsoRepositorio.findByProyectoId(proyectoId);
        String filename = "matriz_trazabilidad_" + proyectoId + ".pdf";
        byte[] pdf = exportacionServicio.exportarMatrizTrazabilidadPDF(proyectoId, requisitos, casosUso);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8))
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
    
    // ========== EXPORTAR HISTORIAS DE USUARIO A PDF ==========
    @GetMapping("/historias/pdf/{proyectoId}")
    public ResponseEntity<byte[]> exportarHistoriasPDF(@PathVariable String proyectoId) throws DocumentException {
        List<HistoriaUsuario> historias = historiaRepositorio.findAll();
        String filename = "historias_" + proyectoId + ".pdf";
        byte[] pdf = exportacionServicio.exportarHistoriasPDF(historias);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8))
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
    
    // ========== EXPORTAR GLOSARIO A PDF ==========
    @GetMapping("/glosario/pdf/{proyectoId}")
    public ResponseEntity<byte[]> exportarGlosarioPDF(@PathVariable String proyectoId) throws DocumentException {
        List<GlosarioTermino> terminos = glosarioRepositorio.findByProyectoId(proyectoId);
        String filename = "glosario_" + proyectoId + ".pdf";
        byte[] pdf = exportacionServicio.exportarGlosarioPDF(terminos);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8))
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
    
    // ========== EXPORTAR CASOS DE USO A WORD ==========
    @GetMapping("/casos-uso/word/{proyectoId}")
    public ResponseEntity<byte[]> exportarCasosUsoWord(@PathVariable String proyectoId) throws IOException {
        List<CasoUso> casosUso = casoUsoRepositorio.findByProyectoId(proyectoId);
        String filename = "casos_uso_" + proyectoId + ".docx";
        byte[] word = exportacionServicio.exportarCasosUsoWord(casosUso);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(word);
    }
}