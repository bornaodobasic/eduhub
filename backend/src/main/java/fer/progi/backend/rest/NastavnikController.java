package fer.progi.backend.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import fer.progi.backend.dto.RasporedDTO;
import fer.progi.backend.service.impl.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import fer.progi.backend.domain.*;
import fer.progi.backend.dto.PredmetMaterijaliDTO;
import fer.progi.backend.dto.PristupMaterijaliDTO;
import fer.progi.backend.service.DjelatnikService;
import fer.progi.backend.service.NastavnikService;
import fer.progi.backend.service.ObavijestService;
import fer.progi.backend.service.PredmetService;
import fer.progi.backend.service.UcenikService;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/nastavnik")
@PreAuthorize("hasAuthority('Nastavnik')")
public class NastavnikController {

    @Autowired
    private NastavnikService nastavnikService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ObavijestService obavijestService;

    @Autowired
    private PredmetService predmetServise;

    @Autowired
    private UcenikService ucenikService;

    @Autowired
    private DjelatnikService djelatnikService;



    @PostMapping("")
    public Nastavnik dodajNastavnika(@RequestBody Nastavnik nastavnik) {
        return nastavnikService.dodajNastavnik(nastavnik);
    }

    @GetMapping("")
    public List<Map<String, String>> getNastavniciMailovi(Authentication authentication) {
        List<Map<String, String>> nastavnici = new ArrayList<>();

        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String email = oidcUser.getAttribute("preferred_username");

        for (Nastavnik n : nastavnikService.findAllNastavniks()) {
            if (!n.getEmail().equals(email)) {
                Map<String, String> nastavnikInfo = new HashMap<>();
                nastavnikInfo.put("email", n.getEmail());
                nastavnikInfo.put("ime", n.getImeNastavnik() + " " + n.getPrezimeNastavnik());
                nastavnici.add(nastavnikInfo);
            }
        }

        return nastavnici;
    }

    @GetMapping("/ucenici")
    public List<Map<String, String>> getUceniciMailovi() {
        List<Map<String, String>> ucenici = new ArrayList<>();

        for (Ucenik u : ucenikService.findAllUceniks()) {
            Map<String, String> ucenikInfo = new HashMap<>();
            ucenikInfo.put("email", u.getEmail());
            ucenikInfo.put("ime", u.getImeUcenik() + " " + u.getPrezimeUcenik());
            ucenici.add(ucenikInfo);
        }

        return ucenici;
    }

    @GetMapping("/predmeti")
    public ResponseEntity<?> getPredmeti(@AuthenticationPrincipal OidcUser oidcUser) {
        try {
            String email = oidcUser.getPreferredUsername();
            System.out.println(email);
            List<Predmet> predmeti = nastavnikService.findNastavnikPredmeti(email);

            List<PredmetMaterijaliDTO> response = predmeti.stream().map(predmet -> {
                List<String> materijali = s3Service.listFiles(predmet.getNazPredmet() + "/");
                return new PredmetMaterijaliDTO(predmet.getSifPredmet(), predmet.getNazPredmet(), materijali);
            }).collect(Collectors.toList());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("OOPS! Something went wrong...");
        }
    }

    @PostMapping("/materijali")
    public ResponseEntity<?> uploadMaterial(
            @RequestParam String predmet,
            @RequestParam MultipartFile file
    ) {
        try {
            String path = predmet;
            String message = s3Service.uploadFile(path, file);
            return ResponseEntity.ok(message);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("OOPS! Something went wrong");
        }
    }

    @DeleteMapping("/materijali")
    public ResponseEntity<?> deleteMaterial(@RequestParam String key) {
        try {
            s3Service.deleteFile(key);
            return ResponseEntity.ok("Materijal uspješno obrisan!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("OOPS! Something went wrong");
        }
    }

    @GetMapping("materijali") //ovdje treba povuc ime predmeta npr. Biologija_1_opca1
    public ResponseEntity<List<String>> MaterijaliPredmet(@RequestParam String predmet) {
        try {
            String prefix = predmet + "/";
            List<String> materijali = s3Service.listFiles(prefix);
            return ResponseEntity.ok(materijali);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }




    @GetMapping("/materijali/izvjestaj")
    public List<PristupMaterijaliDTO> pregledPristupaMaterijalima(Authentication authentication) throws ParseException {
        List<PristupMaterijaliDTO> listaPristupMaterijalima = new ArrayList<>();
        String csvFileKey = "materijali/materijalinew.csv"; // Ključ CSV datoteke na S3
        Path tempFilePath = null;

        try {

            tempFilePath = Files.createTempFile("temp-materijali", ".csv");
            System.out.println(tempFilePath);


            byte[] csvContent = s3Service.getFile(csvFileKey);
            Files.write(tempFilePath, csvContent);


            OidcUser ulogiranKorisnik = (OidcUser) authentication.getPrincipal();
            String email = ulogiranKorisnik.getPreferredUsername();
            List<Predmet> predmetiNastavnika = nastavnikService.findNastavnikPredmeti(email);


            try (BufferedReader reader = Files.newBufferedReader(tempFilePath)) {
                String line;


                while ((line = reader.readLine()) != null) {


                    System.out.println(line);
                    if (line.trim().isEmpty()) {
                        continue;
                    }

                    String[] row = line.split(",");


                    for (Predmet p : predmetiNastavnika) {
                        if (p.getNazPredmet().equals(row[5])) {
                            listaPristupMaterijalima.add(new PristupMaterijaliDTO(
                                    row[0],
                                    row[1],
                                    row[2],
                                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(row[3]),
                                    row[4],
                                    row[5]
                            ));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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

        return listaPristupMaterijalima;
    }

    @PostMapping("/obavijesti")
    public ResponseEntity<String> dodajObavijest(
            @RequestParam String naslovObavijest,
            @RequestParam String sadrzajObavijest,
            @RequestParam String nazPredmet,
            @RequestParam(required = false) String adresaLokacija,
            @RequestParam(required = false) String gradLokacija,
            @RequestParam(required = false) String drzavaLokacija,
            Authentication authentication) {

        OidcUser ulogiranKorisnik = (OidcUser) authentication.getPrincipal();
        String email = ulogiranKorisnik.getPreferredUsername();
        Optional<Nastavnik> optionalnastavnik = nastavnikService.findByEmail(email);
        Nastavnik nastavnik = optionalnastavnik.get();
        String imeNastavnik = nastavnik.getImeNastavnik();
        String prezimeNastavnik = nastavnik.getPrezimeNastavnik();

        Date datumObavijest = new Date();


        obavijestService.dodajObavijest(
                naslovObavijest,
                sadrzajObavijest,
                datumObavijest,
                nazPredmet,
                imeNastavnik,
                prezimeNastavnik,
                adresaLokacija,
                gradLokacija,
                drzavaLokacija);

        return ResponseEntity.ok("Obavijest uspješno dodana");
    }

    @GetMapping("/obavijesti")
    public ResponseEntity<?> pogledajObavijesti(@RequestParam String nazPredmet){
        List<Obavijest> obavijesti = obavijestService.prikaziObavijestiZaPredmet(nazPredmet);
        return ResponseEntity.ok(obavijesti);
    }

    @DeleteMapping("/obavijesti")
    public ResponseEntity<?> izbrisiObavijest(@RequestParam int sifObavijest){
        obavijestService.obrisiObavijest(sifObavijest);
        return ResponseEntity.ok("Obavijest uspjesno obrisana");
    }

    @GetMapping("/uceniciNaPredmetu")
    public ResponseEntity<?> pogledajUcenikeNaPredmetu(@RequestParam String nazPredmet){
        Predmet predmet = predmetServise.findPredmetByNaz(nazPredmet);

        Smjer smjer = predmet.getSmjer();

        List<Razred> razredi = smjer.getRazredi();


        List<String> nazivirazreda = new ArrayList();

        for(Razred r: razredi) {
            nazivirazreda.add(r.getNazRazred());
        }

        List<Ucenik> ucenici = ucenikService.findAllUceniks();

        // Prvi kriterij: Filtriraj učenike iz valjanih razreda
        ucenici = ucenici.stream()
                .filter(c -> nazivirazreda.contains(c.getRazred().getNazRazred()))
                .collect(Collectors.toList());


        ucenici = ucenici.stream()
                .filter(c -> !(c.getVjeronauk() && nazPredmet.startsWith("Etika")) &&
                        !(nazPredmet.startsWith("Vjeronauk") && !c.getVjeronauk()))
                .collect(Collectors.toList());


        return ResponseEntity.ok(ucenici);
    }

    @GetMapping("/{email}/raspored")
    public List<RasporedDTO> getRaspored(@PathVariable String email) {
        return djelatnikService.getRasporedZaNastavnika(email);
    }

}