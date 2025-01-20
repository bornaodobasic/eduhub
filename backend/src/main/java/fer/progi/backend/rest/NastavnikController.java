package fer.progi.backend.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import fer.progi.backend.domain.Predmet;
import fer.progi.backend.service.impl.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.service.NastavnikService;
import fer.progi.backend.service.ObavijestService;

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


    @PostMapping("")
    public Nastavnik dodajNastavnika(@RequestBody Nastavnik nastavnik) {
        return nastavnikService.dodajNastavnik(nastavnik);
    }

    @GetMapping("/")
    public List<String> getNastavniciMailovi(Authentication authentication) {
        List<String> mailoviNastavnika = new ArrayList<>();

        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String email = oidcUser.getAttribute("preferred_username");

        for (Nastavnik n : nastavnikService.findAllNastavniks()) {
            if (!n.getEmail().equals(email))
                mailoviNastavnika.add(n.getEmail());
        }

        return mailoviNastavnika;
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

    @GetMapping("{predmet}/dodajObavijest")
    public ResponseEntity<String> dodajObavijestZaPredmet(
            @PathVariable String predmet,
            @RequestParam String naslov,
            @RequestParam String sadrzaj,
            Authentication authentication) {


        String csvFileKey = "obavijesti/" + predmet + ".csv";
        Path tempFilePath = null;

        try {

            tempFilePath = Files.createTempFile(predmet + "-obavijesti", ".csv");
            System.out.println("Privremena datoteka: " + tempFilePath);


            try {
                byte[] fileContent = s3Service.getFile(csvFileKey);
                Files.write(tempFilePath, fileContent);
            } catch (Exception e) {

                String zaglavlje = "Naslov,Sadrzaj,ImeNastavnika,PrezimeNastavnikaDatumVrijeme\n";
                Files.write(tempFilePath, zaglavlje.getBytes());
            }


            OidcUser ulogiranKorisnik = (OidcUser) authentication.getPrincipal();
            String email = ulogiranKorisnik.getPreferredUsername();
            Optional<Nastavnik> optionalnastavnik = nastavnikService.findByEmail(email);


            Nastavnik nastavnik = optionalnastavnik.get();


            String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));


            String newLine = naslov + "," + sadrzaj + "," + nastavnik.getImeNastavnik() + "," + nastavnik.getPrezimeNastavnik() + "," + currentDateTime + "\n";
            Files.write(tempFilePath, newLine.getBytes(), StandardOpenOption.APPEND);


            s3Service.uploadFileFromPath(csvFileKey, tempFilePath);

            return ResponseEntity.ok("Obavijest za predmet " + predmet + " uspješno dodana.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Dogodila se greška.");
        } finally {
            if (tempFilePath != null) {
                try {
                    Files.delete(tempFilePath);
                } catch (IOException e) {
                    System.err.println("Greška pri brisanju privremene datoteke: " + e.getMessage());
                }
            }
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

    @GetMapping("/materijali/izvjestaj")
    public List<PristupMaterijaliDTO> pregledPristupaMaterijalima(Authentication authentication) throws ParseException {
        List<PristupMaterijaliDTO> listaPristupMaterijalima = new ArrayList<>();
        String csvFileKey = "materijali/materijali.csv"; // Ključ CSV datoteke na S3
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
                boolean isFirstLine = true;

                while ((line = reader.readLine()) != null) {

                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }


                    if (line.trim().isEmpty()) {
                        continue;
                    }

                    String[] row = line.split(",");


                    for (Predmet p : predmetiNastavnika) {
                        if (p.getNazPredmet().equals(row[4])) {
                            listaPristupMaterijalima.add(new PristupMaterijaliDTO(
                                    row[0],
                                    row[1],
                                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(row[2]),
                                    row[3],
                                    row[4]
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
    
    @GetMapping("/dodajObavijest")
	public ResponseEntity<String> dodajObavijest(
	            @RequestParam String naslovObavijest,
	            @RequestParam String sadrzajObavijest,
	            @RequestParam Integer sifPredmet,
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
                sifPredmet,
                imeNastavnik,
                prezimeNastavnik,
                adresaLokacija,
                gradLokacija,
                drzavaLokacija);

        return ResponseEntity.ok("Obavijest uspješno dodana");
	}


}
