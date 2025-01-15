package fer.progi.backend.rest;

import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.service.*;
import fer.progi.backend.service.impl.S3Service;
import fer.progi.backend.domain.Aktivnost;
import fer.progi.backend.domain.Predmet;
import fer.progi.backend.domain.Ucenik;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/ucenik")
@PreAuthorize("hasAuthority('Ucenik')")

public class UcenikController {

    @Autowired
    private UcenikService ucenikService;
    
    @Autowired
    private UcenikAktivnostService ucenikServiceAktivnosti;

    @Autowired
    private PDFService pdfService;

    @Autowired
    private MailService mailService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private NastavnikService nastavnikService;
    
    @GetMapping("")
    public List<String> getUceniciMailovi(Authentication authentication) {
    	List<String> mailoviUcenika = new ArrayList<>();
    	
    	OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
 	    String email = oidcUser.getAttribute("preferred_username");
    	
    	for(Ucenik u :ucenikService.findAllUceniks()) {
    		if(!u.getEmail().equals(email))
    		mailoviUcenika.add(u.getEmail());
    	}
    	
    	return mailoviUcenika;
    }

    @GetMapping("/aktivnosti/je/{email}")
    public List<Aktivnost> getUceniciAktivnosti(@PathVariable String email) {
        Optional<Ucenik> ucenikOptional = ucenikService.findByEmailUcenik(email);
        if (ucenikOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return ucenikService.findUcenikAktivnosti(email);
    }

    @GetMapping("/aktivnosti/nije/{email}")
    public Set<Aktivnost> getNotUcenikAktivnosti(@PathVariable String email) {
        Optional<Ucenik> ucenikOptional = ucenikService.findByEmailUcenik(email);
        if(ucenikOptional.isEmpty()) {
            return null;
        }
        return ucenikServiceAktivnosti.findNotUcenikAktivnosti(email);
    }
    @GetMapping("/nastavnici")
    public List<String> getNastavniciMailovi() {
        List<String> mailoviNastavnika = new ArrayList<>();
        for(Nastavnik n :nastavnikService.findAllNastavniks()) {
            mailoviNastavnika.add(n.getEmail());
        }

        return mailoviNastavnika;
    }

    @PostMapping("/dodajAktivnosti")
    public ResponseEntity<String> dodajAktivnosti(Authentication authentication, @RequestBody List<String> oznAktivnosti) {
        LocalDate krajnjiRok = LocalDate.of(2025, 9, 1);

        if (LocalDate.now().isAfter(krajnjiRok)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Više nije moguće prijaviti aktivnosti jer je prošao rok.");
        }
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String email = (String) oidcUser.getAttributes().get("preferred_username");

        boolean success = ucenikService.dodajAktivnostiPoNazivu(email, oznAktivnosti);

        if (success) {
            return ResponseEntity.ok("Aktivnosti su uspješno dodane.");
        } else {
            return ResponseEntity.badRequest().body("Dodavanje aktivnosti nije uspjelo.");
        }

    }

    @GetMapping("/predmeti")
    public ResponseEntity<List<Predmet>> dohvatiSveupisanePredmete(Authentication authentification) {


        OidcUser ulogiranKorisnik = (OidcUser) authentification.getPrincipal();
        String email = ulogiranKorisnik.getPreferredUsername();
        System.out.println(email);

        List<Predmet> predmeti = ucenikService.listAllPredmeti(email);

        return ResponseEntity.ok(predmeti);
    }


    @PostMapping("/{email}/generirajPotvrdu")
    public ResponseEntity<byte[]> generirajPotvrdu(@PathVariable String email) {
        Optional<Ucenik> ucenikOptional = ucenikService.findByEmailUcenik(email);

        if (ucenikOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Ucenik ucenik = ucenikOptional.get();

        byte[] pdfBytes = pdfService.generatePDF(ucenik.getImeUcenik(), ucenik.getPrezimeUcenik());

        Path writableDir = Paths.get(System.getProperty("user.dir"), "backend/src/main/resources");
        Path csvFilePath = writableDir.resolve("db/zahtjevi.csv");

        try {
            Files.createDirectories(writableDir.resolve("db"));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        try (BufferedWriter writerCSV = Files.newBufferedWriter(csvFilePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
             PrintWriter pw = new PrintWriter(writerCSV)) {

            String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            pw.println(ucenik.getImeUcenik() + "," + ucenik.getPrezimeUcenik() + "," + currentDateTime);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=potvrda.pdf")
                .header("Content-Type", "application/pdf")
                .body(pdfBytes);
    }



    @PostMapping("/{email}/posaljiMail")
    public ResponseEntity<String> posaljiNaMail(@PathVariable String email) {

        Optional<Ucenik> ucenikOptional = ucenikService.findByEmailUcenik(email);

        if (ucenikOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Ucenik ucenik = ucenikOptional.get();

        byte[] pdfBytes = pdfService.generatePDF(ucenik.getImeUcenik(), ucenik.getPrezimeUcenik());

        mailService.sendMail(email, pdfBytes, "potvrda_" + ucenik.getImeUcenik() + "_" + ucenik.getPrezimeUcenik() + ".pdf");

        String csvFilePath = "database/zahtjevi.csv";


        try (BufferedWriter writerCSV = Files.newBufferedWriter(Paths.get(csvFilePath), StandardOpenOption.APPEND);
             PrintWriter pw = new PrintWriter(writerCSV)) {

            String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            pw.println(ucenik.getImeUcenik() + "," + ucenik.getPrezimeUcenik() + "," + currentDateTime);
        } catch (Exception e) {

        }

        return ResponseEntity.ok("Mail uspješno poslan");


    }

    @GetMapping("{predmet}/materijali") //ovdje treba povuc ime predmeta npr. Biologija_1_opca1
    public ResponseEntity<List<String>> MaterijaliPredmet(@PathVariable String predmet) {
        try {
            String prefix = predmet + "/";
            List<String> materijali = s3Service.listFiles(prefix);
            return ResponseEntity.ok(materijali);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("{predmet}/materijali/download")
    public ResponseEntity<byte[]> downloadMaterialBySuffix(@RequestParam String suffix, Authentication authentification) {
        try {

            String key = s3Service.findFileBySuffix(suffix);
            if (key == null) {
                return ResponseEntity.status(404).body(null);
            }
            byte[] content = s3Service.getFile(key);
            String fileName = key.substring(key.lastIndexOf("/") + 1);
            String prefix = s3Service.extractPrefix(key);


            OidcUser ulogiranKorisnik = (OidcUser) authentification.getPrincipal();
            String email = ulogiranKorisnik.getPreferredUsername();

            Optional<Ucenik> ucenikOptional = ucenikService.findByEmailUcenik(email);

            Path writableDir = Paths.get(System.getProperty("user.dir"), "backend/src/main/resources");
            Path csvFilePath = writableDir.resolve("db/materijali.csv");

            try {
                Files.createDirectories(writableDir.resolve("db"));
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }

            Ucenik ucenik = ucenikOptional.get();

            try (BufferedWriter writerCSV = Files.newBufferedWriter(csvFilePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                 PrintWriter pw = new PrintWriter(writerCSV)) {

                String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                pw.println(ucenik.getImeUcenik() + "," + ucenik.getPrezimeUcenik() + "," + currentDateTime + ',' + suffix + ',' + prefix + '\n');
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + suffix)
                    .contentLength(content.length)
                    .body(content);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{email}/raspored")
    public List<RasporedDTO> getRaspored(@PathVariable String email) {
        return ucenikService.getRaspored(email);
    }


}

