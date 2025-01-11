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

           
            BaseFont baseFont = BaseFont.createFont(
                    getClass().getClassLoader().getResource("fonts/arial.ttf").getPath(),
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED
            );
            Font customFont = new Font(baseFont, 12, Font.NORMAL);
            Font titleFont = new Font(baseFont, 18, Font.BOLD);

            
            try {
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("images/hrvatskiGrb.png");
                if (inputStream == null) {
                    throw new FileNotFoundException("Resurs nije pronađen: images/hrvatskiGrb.png");
                }

                byte[] imageBytes = inputStream.readAllBytes();
                Image grb = Image.getInstance(imageBytes);

                grb.scaleToFit(50, 50);
                grb.setAlignment(Image.LEFT);
                document.add(grb);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Greška pri učitavanju slike grba.");
            }

            // Naslov
            Paragraph title = new Paragraph("ELEKTRONIČKI ZAPIS O STATUSU UČENIKA", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n\n\n"));

            
            Paragraph content = new Paragraph(
                    "Potvrđujemo da je učenik: " + ime + " " + prezime + " upisan u školsku godinu 2025./2026.",
                    customFont
            );
            content.setAlignment(Element.ALIGN_LEFT);
            document.add(content);

            document.add(new Paragraph("\n\n\n"));


            try {
                InputStream inputStreamLogo = getClass().getClassLoader().getResourceAsStream("images/Eduhub.png");
                if (inputStreamLogo == null) {
                    throw new FileNotFoundException("Resurs nije pronađen: images/logo.png");
                }

                byte[] logoBytes = inputStreamLogo.readAllBytes();
                Image logo = Image.getInstance(logoBytes);
                logo.scaleToFit(200, 150);
                logo.setAbsolutePosition(36, 50);
                writer.getDirectContent().addImage(logo);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Greška pri učitavanju slike logotipa.");
            }

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
          

          

        } catch (Exception e) {
           
        }

        return outputStream.toByteArray();
    }


}
