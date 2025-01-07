package fer.progi.backend.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;

import fer.progi.backend.service.PDFService;

import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class PDFServiceJPA implements PDFService {

    public byte[] generatePDF(String ime, String prezime) {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

           
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Potvrda o upisu", titleFont);
            title.setAlignment(Element.ALIGN_CENTER); 
            document.add(title);

            document.add(new Paragraph("\n")); 

          
            Font textFont = new Font(Font.FontFamily.HELVETICA, 12);
            Paragraph content = new Paragraph("Potvrđujemo da je učenik: " + ime + " " + prezime, textFont);
            content.setAlignment(Element.ALIGN_LEFT);
            document.add(content);

            document.add(new Paragraph("\n\n\n"));

            
            Font footerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."));
            Paragraph footer = new Paragraph("Potvrda generirana: " + currentDate, footerFont);
            footer.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }
} 