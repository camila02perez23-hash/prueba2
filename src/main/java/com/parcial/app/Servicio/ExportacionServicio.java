package com.parcial.app.Servicio;

import com.itextpdf.text.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.parcial.app.Modelo.*;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExportacionServicio {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    // ========== EXPORTAR PROYECTOS A PDF ==========
    public byte[] exportarProyectosAPDF(List<Proyecto> proyectos) throws DocumentException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();
        
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Reporte de Proyectos - SIGPROD", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));
        
        document.add(new Paragraph("Fecha generación: " + LocalDateTime.now().format(FORMATTER)));
        document.add(new Paragraph(" "));
        
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        
        String[] headers = {"ID", "Nombre", "Metodología", "Presupuesto", "Estado"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }
        
        for (Proyecto p : proyectos) {
            table.addCell(p.getId() != null ? p.getId().substring(0, Math.min(8, p.getId().length())) : "N/A");
            table.addCell(p.getNombre() != null ? p.getNombre() : "N/A");
            table.addCell(p.getMetodologia() != null ? p.getMetodologia() : "N/A");
            table.addCell(p.getPresupuesto() != null ? "$" + p.getPresupuesto() : "$0");
            table.addCell(p.getEstado() != null ? p.getEstado() : "N/A");
        }
        
        document.add(table);
        document.close();
        return out.toByteArray();
    }
    
    // ========== EXPORTAR REQUISITOS A WORD ==========
    public byte[] exportarRequisitosAWord(List<Requisito> requisitos) throws IOException {
        XWPFDocument document = new XWPFDocument();
        
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = title.createRun();
        titleRun.setText("Especificación de Requisitos - SIGPROD");
        titleRun.setFontSize(18);
        titleRun.setBold(true);
        titleRun.addBreak();
        
        XWPFParagraph date = document.createParagraph();
        date.createRun().setText("Fecha: " + LocalDateTime.now().format(FORMATTER));
        date.createRun().addBreak();
        date.createRun().addBreak();
        
        XWPFTable table = document.createTable(requisitos.size() + 1, 6);
        table.setWidth("100%");
        
        String[] headers = {"Código", "Nombre", "Tipo", "Prioridad", "Estado", "Versión"};
        for (int i = 0; i < headers.length; i++) {
            XWPFParagraph cellPara = table.getRow(0).getCell(i).getParagraphs().get(0);
            XWPFRun run = cellPara.createRun();
            run.setText(headers[i]);
            run.setBold(true);
        }
        
        for (int i = 0; i < requisitos.size(); i++) {
            Requisito r = requisitos.get(i);
            table.getRow(i + 1).getCell(0).setText(r.getCodigo() != null ? r.getCodigo() : "N/A");
            table.getRow(i + 1).getCell(1).setText(r.getNombre() != null ? r.getNombre() : "N/A");
            table.getRow(i + 1).getCell(2).setText(r.getTipo() != null ? r.getTipo() : "N/A");
            table.getRow(i + 1).getCell(3).setText(r.getPrioridad() != null ? r.getPrioridad() : "N/A");
            table.getRow(i + 1).getCell(4).setText(r.getEstado() != null ? r.getEstado() : "N/A");
            table.getRow(i + 1).getCell(5).setText(r.getVersion() != null ? String.valueOf(r.getVersion()) : "1");
        }
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        document.write(out);
        document.close();
        return out.toByteArray();
    }
    
    // ========== EXPORTAR MATRIZ DE TRAZABILIDAD A PDF ==========
    public byte[] exportarMatrizTrazabilidadPDF(String proyectoId, List<Requisito> requisitos, 
                                                  List<CasoUso> casosUso) throws DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();
        
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("Matriz de Trazabilidad - SIGPROD", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        
        Paragraph subtitle = new Paragraph("Proyecto ID: " + proyectoId);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        document.add(subtitle);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Fecha: " + LocalDateTime.now().format(FORMATTER)));
        document.add(new Paragraph(" "));
        
        PdfPTable table = new PdfPTable(casosUso.size() + 1);
        table.setWidthPercentage(100);
        
        PdfPCell cell = new PdfPCell(new Phrase("Requisito / Caso de Uso", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        
        for (CasoUso cu : casosUso) {
            cell = new PdfPCell(new Phrase(cu.getNombre() != null ? cu.getNombre() : "N/A", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }
        
        if (requisitos.isEmpty()) {
            PdfPCell emptyCell = new PdfPCell(new Phrase("No hay requisitos registrados"));
            emptyCell.setColspan(casosUso.size() + 1);
            table.addCell(emptyCell);
        } else {
            for (Requisito r : requisitos) {
                table.addCell(r.getNombre() != null ? r.getNombre() : "N/A");
                for (CasoUso cu : casosUso) {
                    String marcador = (cu.getRequisitoId() != null && cu.getRequisitoId().equals(r.getId())) ? "✓" : "—";
                    table.addCell(marcador);
                }
            }
        }
        
        document.add(table);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Leyenda: ✓ = Vinculado | — = Sin vinculación"));
        document.close();
        return out.toByteArray();
    }
    
    // ========== EXPORTAR GLOSARIO A PDF ==========
    public byte[] exportarGlosarioPDF(List<GlosarioTermino> terminos) throws DocumentException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();
        
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Glosario de Términos - SIGPROD", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Fecha: " + LocalDateTime.now().format(FORMATTER)));
        document.add(new Paragraph(" "));
        
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        
        String[] headers = {"Término", "Definición", "Categoría"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }
        
        for (GlosarioTermino t : terminos) {
            table.addCell(t.getTermino() != null ? t.getTermino() : "N/A");
            table.addCell(t.getDefinicion() != null ? t.getDefinicion() : "N/A");
            table.addCell(t.getCategoria() != null ? t.getCategoria() : "N/A");
        }
        
        document.add(table);
        document.close();
        return out.toByteArray();
    }
    
    // ========== EXPORTAR CASOS DE USO A WORD ==========
    public byte[] exportarCasosUsoWord(List<CasoUso> casosUso) throws IOException {
        XWPFDocument document = new XWPFDocument();
        
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = title.createRun();
        titleRun.setText("Casos de Uso - SIGPROD");
        titleRun.setFontSize(18);
        titleRun.setBold(true);
        titleRun.addBreak();
        
        XWPFParagraph date = document.createParagraph();
        date.createRun().setText("Fecha: " + LocalDateTime.now().format(FORMATTER));
        date.createRun().addBreak();
        date.createRun().addBreak();
        
        for (CasoUso cu : casosUso) {
            XWPFParagraph cuTitle = document.createParagraph();
            cuTitle.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun cuRun = cuTitle.createRun();
            cuRun.setText("Caso de Uso: " + (cu.getNombre() != null ? cu.getNombre() : "Sin nombre"));
            cuRun.setFontSize(14);
            cuRun.setBold(true);
            cuRun.addBreak();
            
            XWPFParagraph actor = document.createParagraph();
            actor.createRun().setText("Actor: " + (cu.getActor() != null ? cu.getActor() : "No especificado"));
            actor.createRun().addBreak();
            
            XWPFParagraph flujoBasico = document.createParagraph();
            flujoBasico.createRun().setText("Flujo Básico:");
            flujoBasico.createRun().setBold(true);
            flujoBasico.createRun().addBreak();
            flujoBasico.createRun().setText(cu.getFlujoBasico() != null ? cu.getFlujoBasico() : "No especificado");
            flujoBasico.createRun().addBreak();
            flujoBasico.createRun().addBreak();
            
            XWPFParagraph flujosAlt = document.createParagraph();
            flujosAlt.createRun().setText("Flujos Alternos:");
            flujosAlt.createRun().setBold(true);
            flujosAlt.createRun().addBreak();
            if (cu.getFlujosAlternos() != null && !cu.getFlujosAlternos().isEmpty()) {
                for (String alt : cu.getFlujosAlternos()) {
                    flujosAlt.createRun().setText("- " + alt);
                    flujosAlt.createRun().addBreak();
                }
            } else {
                flujosAlt.createRun().setText("No especificado");
                flujosAlt.createRun().addBreak();
            }
            flujosAlt.createRun().addBreak();
            
            XWPFParagraph pre = document.createParagraph();
            pre.createRun().setText("Precondiciones: " + (cu.getPrecondiciones() != null ? cu.getPrecondiciones() : "Ninguna"));
            pre.createRun().addBreak();
            
            XWPFParagraph post = document.createParagraph();
            post.createRun().setText("Postcondiciones: " + (cu.getPostcondiciones() != null ? cu.getPostcondiciones() : "Ninguna"));
            post.createRun().addBreak();
            post.createRun().addBreak();
            
            XWPFParagraph separator = document.createParagraph();
            separator.createRun().setText("─────────────────────────────────");
            separator.createRun().addBreak();
        }
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        document.write(out);
        document.close();
        return out.toByteArray();
    }

	public byte[] exportarHistoriasPDF(List<HistoriaUsuario> historias) {
		// TODO Auto-generated method stub
		return null;
	}
}