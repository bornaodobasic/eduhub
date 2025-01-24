package fer.progi.backend.rest;

import fer.progi.backend.domain.*;
import fer.progi.backend.service.*;
import fer.progi.backend.service.impl.S3Service;
import fer.progi.backend.domain.Aktivnost;
import fer.progi.backend.dto.RasporedDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Autowired
    private ObavijestService obavijestService;

    @GetMapping("")
    public List<Map<String, String>> getUceniciMailovi(Authentication authentication) {
        List<Map<String, String>> ucenici = new ArrayList<>();

        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String email = oidcUser.getAttribute("preferred_username");

        for (Ucenik u : ucenikService.findAllUceniks()) {
            if (!u.getEmail().equals(email)) {
                Map<String, String> ucenikInfo = new HashMap<>();
                ucenikInfo.put("email", u.getEmail());
                ucenikInfo.put("ime", u.getImeUcenik() + " " + u.getPrezimeUcenik());
                ucenici.add(ucenikInfo);
            }
        }

        return ucenici;
    }

    @GetMapping("/nastavnici")
    public List<Map<String, String>> getNastavniciMailovi() {
        List<Map<String, String>> nastavnici = new ArrayList<>();

        for (Nastavnik n : nastavnikService.findAllNastavniks()) {
            Map<String, String> nastavnikInfo = new HashMap<>();
            nastavnikInfo.put("email", n.getEmail());
            nastavnikInfo.put("ime", n.getImeNastavnik() + " " + n.getPrezimeNastavnik());
            nastavnici.add(nastavnikInfo);
        }

        return nastavnici;
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


        String csvFileKey = "zahtjevi/zahtjevinew.csv";


        Path tempFilePath = null;
        try {

            tempFilePath = Files.createTempFile("temp-zahtjevi", ".csv");
            System.out.println(tempFilePath);

            try {
                byte[] fileContent = s3Service.getFile(csvFileKey);
                Files.write(tempFilePath, fileContent, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (Exception e) {

                System.out.println("Datoteka ne postoji na S3, stvara se nova.");
            }


            String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String newLine = ucenik.getImeUcenik() + "," + ucenik.getPrezimeUcenik() +"," + email + "," + currentDateTime;
            Files.write(tempFilePath, (newLine + "\n").getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);


            s3Service.uploadFileFromPath(csvFileKey, tempFilePath);
            System.out.println("Upload");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } finally {

            if (tempFilePath != null) {
                try {
                    Files.delete(tempFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Nije moguće obrisati privremenu datoteku: " + tempFilePath);
                }
            }
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

        mailService.sendMail(email, pdfBytes, "potvrda_" + ucenik.getImeUcenik() + "_" + ucenik.getPrezimeUcenik() + ".pdf" );


        String csvFileKey = "zahtjevi/zahtjevinew.csv";


        Path tempFilePath = null;
        try {

            tempFilePath = Files.createTempFile("temp-zahtjevi", ".csv");


            try {
                byte[] fileContent = s3Service.getFile(csvFileKey);
                Files.write(tempFilePath, fileContent, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (Exception e) {

                System.out.println("Datoteka ne postoji na S3, stvara se nova.");
            }


            String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String newLine = ucenik.getImeUcenik() + "," + ucenik.getPrezimeUcenik() + "," + email + "," + currentDateTime;
            Files.write(tempFilePath, (newLine + "\n").getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);


            s3Service.uploadFileFromPath(csvFileKey, tempFilePath);
            System.out.println("Upload");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } finally {

            if (tempFilePath != null) {
                try {
                    Files.delete(tempFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Nije moguće obrisati privremenu datoteku: " + tempFilePath);
                }
            }
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




    @GetMapping("/obavijesti") //
    public ResponseEntity<List<Obavijest>> dohvatiSveObavijesti(){
        List<Obavijest> opceObavijesti = obavijestService.prikaziOpceObavijesti();
        return ResponseEntity.ok(opceObavijesti);

    }

    @GetMapping("{nazPredmet}/obavijesti")
    public ResponseEntity<List<Obavijest>> dohvatiObavijestiZaPredmet(@PathVariable String nazPredmet){
        List<Obavijest> predmetObavijest = obavijestService.prikaziObavijestiZaPredmet(nazPredmet);
        return ResponseEntity.ok(predmetObavijest);
    }



    @GetMapping("{predmet}/materijali/download")
    public ResponseEntity<byte[]> downloadMaterialBySuffix(@RequestParam String suffix, Authentication authentication) {
        try {

            String key = s3Service.findFileBySuffix(suffix);
            if (key == null) {
                return ResponseEntity.status(404).body(null);
            }


            byte[] content = s3Service.getFile(key);
            String fileName = key.substring(key.lastIndexOf("/") + 1);
            String prefix = s3Service.extractPrefix(key);


            String csvFileKey = "materijali/materijalinew.csv";
            Path tempFilePath = null;

            try {

                tempFilePath = Files.createTempFile("temp-materijali", ".csv");
                System.out.println(tempFilePath);
                System.out.println(tempFilePath);


                try {
                    byte[] csvContent = s3Service.getFile(csvFileKey);
                    Files.write(tempFilePath, csvContent, StandardOpenOption.TRUNCATE_EXISTING);
                } catch (Exception e) {

                    System.out.println("Datoteka ne postoji na S3, stvara se nova.");
                }


                OidcUser ulogiranKorisnik = (OidcUser) authentication.getPrincipal();
                String email = ulogiranKorisnik.getPreferredUsername();
                Optional<Ucenik> ucenikOptional = ucenikService.findByEmailUcenik(email);

                if (ucenikOptional.isEmpty()) {
                    return ResponseEntity.status(404).body(null);
                }

                Ucenik ucenik = ucenikOptional.get();


                String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String newLine = ucenik.getImeUcenik() + "," + ucenik.getPrezimeUcenik() + "," + email + "," + currentDateTime + "," + suffix + "," + prefix;
                Files.write(tempFilePath, (newLine + "\n").getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);

                s3Service.uploadFileFromPath(csvFileKey, tempFilePath);

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body(null);
            } /*finally {

                if (tempFilePath != null) {
                    try {
                        Files.delete(tempFilePath);
                    } catch (IOException e) {
                        System.err.println("Nije moguće obrisati privremenu datoteku: " + tempFilePath);
                    }
                }
            }*/


            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + fileName)
                    .contentLength(content.length)
                    .body(content);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{email}/raspored")
    public List<RasporedDTO> getRaspored(@PathVariable String email) {
        return ucenikService.getRaspored(email);
    }


}