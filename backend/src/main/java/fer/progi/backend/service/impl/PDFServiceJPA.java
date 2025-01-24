package fer.progi.backend.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import fer.progi.backend.service.PDFService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PDFServiceJPA implements PDFService {

    public byte[] generatePDF(String ime, String prezime) {
        System.out.println("Metoda generatePDF pozvana u: " + LocalDateTime.now());

        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();

            // Učitavanje fonta
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/arial.ttf");
            if (fontStream == null) {
                throw new FileNotFoundException("Font nije pronađen: fonts/arial.ttf");
            }
            BaseFont baseFont = BaseFont.createFont(
                    "arial.ttf",
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED,
                    true,
                    fontStream.readAllBytes(),
                    null
            );
            Font customFont = new Font(baseFont, 12, Font.NORMAL);
            Font titleFont = new Font(baseFont, 18, Font.BOLD);
            System.out.println("Font uspješno učitan");

            // Dodavanje grba
            try {
                InputStream grbStream = getClass().getClassLoader().getResourceAsStream("images/hrvatskiGrb.png");
                if (grbStream == null) {
                    throw new FileNotFoundException("Slika nije pronađena: images/hrvatskiGrb.png");
                }

                byte[] grbBytes = grbStream.readAllBytes();
                Image grb = Image.getInstance(grbBytes);

                grb.scaleToFit(50, 50);
                grb.setAlignment(Image.LEFT);
                document.add(grb);
                System.out.println("Grb uspješno dodan");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Greška pri učitavanju slike grba.");
            }

            // Naslov
            Paragraph title = new Paragraph("ELEKTRONIČKI ZAPIS O STATUSU UČENIKA", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n\n\n"));

            // Sadržaj
            Paragraph content = new Paragraph(
                    "Potvrđujemo da je učenik: " + ime + " " + prezime + " upisan u školsku godinu 2025./2026.",
                    customFont
            );
            content.setAlignment(Element.ALIGN_LEFT);
            document.add(content);

            document.add(new Paragraph("\n\n\n"));

            // Dodavanje logotipa
            try {
                InputStream logoStream = getClass().getClassLoader().getResourceAsStream("images/Eduhub.png");
                if (logoStream == null) {
                    throw new FileNotFoundException("Slika nije pronađena: images/Eduhub.png");
                }

                byte[] logoBytes = logoStream.readAllBytes();
                Image logo = Image.getInstance(logoBytes);
                logo.scaleToFit(200, 150);
                logo.setAbsolutePosition(36, 50);
                writer.getDirectContent().addImage(logo);
                System.out.println("Logotip uspješno dodan");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Greška pri učitavanju slike logotipa.");
            }

            // Footer
            PdfContentByte canvas = writer.getDirectContent();
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."));
            Phrase footerPhrase = new Phrase("Potvrda generirana: " + currentDate, customFont);

            ColumnText.showTextAligned(
                    canvas,
                    Element.ALIGN_RIGHT,
                    footerPhrase,
                    document.getPageSize().getWidth() - 36,
                    110,
                    0
            );

            document.close();
            System.out.println("Dokument uspješno generiran i zatvoren");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Greška pri generiranju PDF-a: " + e.getMessage());
        }

        System.out.println("Veličina generiranog PDF-a: " + outputStream.size());
        return outputStream.toByteArray();
    }
}

