package fer.progi.backend.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import fer.progi.backend.domain.Ravnatelj;
import fer.progi.backend.service.RavnateljService;
import fer.progi.backend.service.impl.S3Service;
import fer.progi.backend.service.UcionicaService;


@RestController
@RequestMapping("/api/ravnatelj")
@PreAuthorize("hasAuthority('Ravnatelj')")
public class RavnateljController {
	
	@Autowired
	private RavnateljService RavnateljService;

    @Autowired
    private UcionicaService ucionicaService;

    @Autowired
    private S3Service s3Service;
	
	@GetMapping("")
	public List<Ravnatelj> listRavnatelj() {
		return RavnateljService.listAll();
	}
	
	@PostMapping("")
	public Ravnatelj dodajRavnatelja(@RequestBody Ravnatelj ravnatelj) {
		return RavnateljService.dodajRavnatelj(ravnatelj);
	}

    @GetMapping("/pogledajIzdanePotvrde")
    public List<ZahtjeviDTO> pregledIzdanihPotvrda() throws ParseException {

        List<ZahtjeviDTO> listaZahtjeva = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/db/zahtjevi.csv")))) {


            String line = reader.readLine();
            if (line == null) return listaZahtjeva;

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length == 3) {
                    listaZahtjeva.add(new ZahtjeviDTO(
                            row[0],
                            row[1],
                            dateFormat.parse(row[2].trim())
                    ));
                } else {
                    System.err.println("Invalid row: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        return listaZahtjeva;
    }


    @GetMapping("/pogledajIzdanePotvrdeImePrezime")
    public List<ZahtjeviDTO> pregledIzdanihPotvrdaImePrezime(@RequestParam String imeUcenik, @RequestParam String prezimeUcenik) throws ParseException {

        List<ZahtjeviDTO> listaZahtjeva = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/db/zahtjevi.csv")))) {


            String line = reader.readLine();
            if (line == null) return listaZahtjeva;

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length == 3 && row[0].equals(imeUcenik) && row[1].equals(prezimeUcenik)) {
                    listaZahtjeva.add(new ZahtjeviDTO(
                            row[0],
                            row[1],
                            dateFormat.parse(row[2].trim())
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
        return listaZahtjeva;
    }

    @GetMapping("/dodajGlavnuObavijest")
    public ResponseEntity<String> dodajGlavnuObavijest(
            @RequestParam String naslov,
            @RequestParam String sadrzaj,
            @RequestParam(required = false) String odredisteAdresa,
            @RequestParam(required = false) String grad,
            @RequestParam(required = false) String drzava) {

        String csvFileKey = "obavijesti/glavneObavijestiNovo.csv";
        Path tempFilePath = null;

        try {

            tempFilePath = Files.createTempFile("glavne-obavijesti", ".csv");
            System.out.println(tempFilePath);


            try {
                byte[] fileContent = s3Service.getFile(csvFileKey);
                Files.write(tempFilePath, fileContent);
            } catch (Exception e) {

                String zaglavlje = "Naslov,Sadrzaj,OdredisteAdresa,Grad,Drzava,DatumVrijeme\n";
                Files.write(tempFilePath, zaglavlje.getBytes());
            }


            String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));


            String newLine = naslov + "," + sadrzaj + "," +
                    (odredisteAdresa != null ? odredisteAdresa : "null") + "," +
                    (grad != null ? grad : "null") + "," +
                    (drzava != null ? drzava : "null") + "," +
                    currentDateTime + "\n";
            Files.write(tempFilePath, newLine.getBytes(), StandardOpenOption.APPEND);


            s3Service.uploadFileFromPath(csvFileKey, tempFilePath);

            return ResponseEntity.ok("Obavijest uspješno dodana.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Dogodila se greška.");
        } finally {

            if (tempFilePath != null) {
                try {
                    Files.delete(tempFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @GetMapping("/dodajObavijestTerenskaNastava")
    public ResponseEntity<String> dodajTerenskuObavijest(
            @RequestParam String naslov,
            @RequestParam String sadrzaj,
            @RequestParam String odredisteAdresa,
            @RequestParam String gradOdrediste,
            @RequestParam String drzavaOdrediste) {

        String csvFileKey = "obavijesti/terenskaNastava.csv";
        Path tempFilePath = null;

        try {

            tempFilePath = Files.createTempFile("terenska-nastava", ".csv");


            try {
                byte[] fileContent = s3Service.getFile(csvFileKey);
                Files.write(tempFilePath, fileContent);
            } catch (Exception e) {

                String zaglavlje = "Naslov,Sadrzaj,OdredisteAdresa,Grad,Drzava,DatumVrijeme\n";
                Files.write(tempFilePath, zaglavlje.getBytes());
                System.out.println("Stvorena nova CSV datoteka.");
            }


            String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));


            String newLine = naslov + "," + sadrzaj + "," + odredisteAdresa + "," + gradOdrediste + "," + drzavaOdrediste + "," + currentDateTime + "\n";
            Files.write(tempFilePath, newLine.getBytes(), java.nio.file.StandardOpenOption.APPEND);


            s3Service.uploadFileFromPath(csvFileKey, tempFilePath);

            return ResponseEntity.ok("Obavijest o terenskoj nastavi uspješno dodana.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Dogodila se greška.");
        } finally {

            if (tempFilePath != null) {
                try {
                    Files.delete(tempFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
}

