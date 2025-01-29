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
import java.util.Date;
import java.util.List;

import fer.progi.backend.domain.Ucionica;
import fer.progi.backend.dto.ObavijestDTO;
import fer.progi.backend.dto.ZahtjeviDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import fer.progi.backend.domain.Obavijest;
import fer.progi.backend.domain.Ravnatelj;
import fer.progi.backend.service.ObavijestService;
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
    private ObavijestService obavijestService;

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

    @GetMapping("/ucionice")
    public List<Ucionica> listAllUcionice() {
        return ucionicaService.findAllUcionice();

    }

    @PostMapping("/ucionice/add")
    public Ucionica addUcionica(@RequestBody Ucionica ucionica) {
        return ucionicaService.dodajUcionica(ucionica);
    }

    @DeleteMapping("/ucionice/delete/{oznakaUc}")
    public ResponseEntity<String> deleteUcenik(@PathVariable String oznakaUc) {
        ucionicaService.deleteUcionica(oznakaUc);
        return ResponseEntity.ok("Uspjesno obrisana ucionica");
    }

    @PutMapping("/ucionica/promijeniKapacitet")
    public ResponseEntity<String> promijeniKapacitet(@PathVariable String oznakaUc, @RequestParam Integer noviKapacitet) {
        if (ucionicaService.findById(oznakaUc).isPresent()) {
            ucionicaService.findById(oznakaUc).get().setKapacitet(noviKapacitet);
            return ResponseEntity.ok("Učionici uspješno promijenjen kapacitet");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Došlo je do problema prilikom mijenjanja kapaciteta učionice");

        }
    }

    @GetMapping("/pogledajIzdanePotvrde")
    public List<ZahtjeviDTO> pregledIzdanihPotvrda() throws ParseException {
        List<ZahtjeviDTO> listaZahtjeva = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String csvFileKey = "zahtjevi/zahtjevinew.csv"; // Ključ datoteke na S3

        Path tempFilePath = null;

        try {

            tempFilePath = Files.createTempFile("zahtjevi", ".csv");


            byte[] csvContent = s3Service.getFile(csvFileKey);
            Files.write(tempFilePath, csvContent);


            try (BufferedReader reader = Files.newBufferedReader(tempFilePath)) {
                String line = reader.readLine();
                if (line == null) return listaZahtjeva;

                while ((line = reader.readLine()) != null) {
                    String[] row = line.split(",");
                    if (row.length == 4) {
                        listaZahtjeva.add(new ZahtjeviDTO(
                                row[0],
                                row[1],
                                row[2],
                                dateFormat.parse(row[3].trim())
                        ));
                    } else {
                        System.err.println("Invalid row format: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error handling CSV file: " + e.getMessage());
        } finally {

            if (tempFilePath != null) {
                try {
                    Files.delete(tempFilePath);
                } catch (IOException e) {
                    System.err.println("Error deleting temporary file: " + e.getMessage());
                }
            }
        }

        return listaZahtjeva;
    }


    @GetMapping("/pogledajIzdanePotvrdeImePrezime")
    public List<ZahtjeviDTO> pregledIzdanihPotvrdaImePrezime(@RequestParam String email) throws ParseException {
        List<ZahtjeviDTO> listaZahtjeva = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String csvFileKey = "zahtjevi/zahtjevinew.csv";

        Path tempFilePath = null;

        try {

            tempFilePath = Files.createTempFile("temp-zahtjevi", ".csv");


            byte[] csvContent = s3Service.getFile(csvFileKey);
            Files.write(tempFilePath, csvContent);


            try (BufferedReader reader = Files.newBufferedReader(tempFilePath)) {
                String line = reader.readLine();
                if (line == null) return listaZahtjeva;

                while ((line = reader.readLine()) != null) {
                    String[] row = line.split(",");
                    if (row[2].equals(email)) {
                        listaZahtjeva.add(new ZahtjeviDTO(
                                row[0],
                                row[1],
                                row[2],
                                dateFormat.parse(row[3].trim())
                        ));
                    } else if (row.length != 3) {
                        System.err.println("Invalid row format: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error handling CSV file: " + e.getMessage());
        } finally {

            if (tempFilePath != null) {
                try {
                    Files.delete(tempFilePath);
                } catch (IOException e) {
                    System.err.println("Error deleting temporary file: " + e.getMessage());
                }
            }
        }

        return listaZahtjeva;
    }


    @PostMapping("/opcaObavijest")
    public ResponseEntity<String> dodajOpcaObavijest(
            @RequestParam String naslovObavijest,
            @RequestParam String sadrzajObavijest,
            Authentication authentication) {


        Date datumObavijest = new Date();


        obavijestService.dodajObavijest(
                naslovObavijest,
                sadrzajObavijest,
                datumObavijest,
                null,
                null,
                null,
                null,
                null,
                null);

        return ResponseEntity.ok("Obavijest uspješno dodana");
    }


    @PostMapping("/terenskaObavijest")
    public ResponseEntity<String> dodajTerenskaObavijest(
            @RequestParam String naslovObavijest,
            @RequestParam String sadrzajObavijest,
            @RequestParam String adresaLokacija,
            @RequestParam String gradLokacija,
            @RequestParam String drzavaLokacija,
            Authentication authentication) {


        Date datumObavijest = new Date();


        obavijestService.dodajObavijest(
                naslovObavijest,
                sadrzajObavijest,
                datumObavijest,
                null,
                null,
                null,
                adresaLokacija,
                gradLokacija,
                drzavaLokacija);

        return ResponseEntity.ok("Obavijest o terenskoj nastavi uspješno dodana");
    }

    @GetMapping("/obavijesti")
    public ResponseEntity<?> pogledajObavijesti(){
        List<Obavijest> opceObavijesti = obavijestService.prikaziOpceObavijesti();
        return ResponseEntity.ok(opceObavijesti);
    }

    @DeleteMapping("/obavijesti")
    public ResponseEntity<?> izbrisiObavijest(@RequestParam int sifObavijest){
        obavijestService.obrisiObavijest(sifObavijest);
        return ResponseEntity.ok("Obavijest uspjesno obrisana");
    }




}
