package fer.progi.backend.rest;

import fer.progi.backend.service.MailService;
import fer.progi.backend.service.PDFService;
import fer.progi.backend.service.UcenikService;
import fer.progi.backend.service.impl.S3Service;
import fer.progi.backend.domain.Predmet;
import fer.progi.backend.domain.Ucenik;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ucenik")
@PreAuthorize("hasAuthority('Ucenik')")
public class UcenikController {

    @Autowired
    private UcenikService ucenikService;
    
    @Autowired
    private PDFService pdfService;
    
    @Autowired 
    private MailService mailService;
    
    @Autowired S3Service s3Service;


    @GetMapping("/")
    public List<String> getUceniciMailovi() {
    	List<String> mailoviUcenika = new ArrayList<>();
        for(Ucenik u :ucenikService.findAllUceniks()) {
            mailoviUcenika.add(u.getEmail());
        }

        return mailoviUcenika;
    }

	@GetMapping("/predmeti")
	public ResponseEntity<List<Predmet>> dohvatiSveupisanePredmete(Authentication authentification){


		OidcUser ulogiranKorisnik = (OidcUser) authentification.getPrincipal();
		String email = ulogiranKorisnik.getPreferredUsername();
		System.out.println(email);

		List<Predmet> predmeti = ucenikService.listAllPredmeti(email);

		return ResponseEntity.ok(predmeti);
	}



    @PostMapping("/dodajAktivnosti")
    public ResponseEntity<String> dodajAktivnosti(Authentication authentication, @RequestBody List<String> oznAktivnosti){
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
	public List<Predmet> listPredmeti(String email) {
        return ucenikService.listAllPredmeti(email);
    }

	@PostMapping("/{email}/generirajPotvrdu")
	public ResponseEntity<byte[]> generirajPotvrdu(@PathVariable String email) {
	    Optional<Ucenik> ucenikOptional = ucenikService.findByEmailUcenik(email);

	    if (ucenikOptional.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); 
	    }

	    Ucenik ucenik = ucenikOptional.get();

	   
	    byte[] pdfBytes = pdfService.generatePDF(ucenik.getImeUcenik(), ucenik.getPrezimeUcenik());
	    
	    
        String csvFilePath = "database/zahjtevi.csv";


        
            try (BufferedWriter writerCSV = Files.newBufferedWriter(Paths.get(csvFilePath), StandardOpenOption.APPEND);
                 PrintWriter pw = new PrintWriter(writerCSV)) {

                String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                pw.println(ucenik.getImeUcenik() + "," + ucenik.getPrezimeUcenik() + "," + currentDateTime);
            }
            catch (Exception e) {

			}



	    return ResponseEntity.ok()
	            .header("Content-Disposition", "attachment; filename=potvrda.pdf")
	            .header("Content-Type", "application/pdf")
	            .body(pdfBytes);
	    
	}

	 @PostMapping("/{email}/posaljiMail")
	 public ResponseEntity<String> posaljiNaMail(@PathVariable String email){
		 
		    Optional<Ucenik> ucenikOptional = ucenikService.findByEmailUcenik(email);

		    if (ucenikOptional.isEmpty()) {
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); 
		    }

		    Ucenik ucenik = ucenikOptional.get();
		    
		    byte[] pdfBytes = pdfService.generatePDF(ucenik.getImeUcenik(), ucenik.getPrezimeUcenik());
		    
		    mailService.sendMail(email, pdfBytes, "potvrda_" + ucenik.getImeUcenik() + "_" + ucenik.getPrezimeUcenik() + ".pdf" );
		    
		    String csvFilePath = "database/zahjtevi.csv";



            try (BufferedWriter writerCSV = Files.newBufferedWriter(Paths.get(csvFilePath), StandardOpenOption.APPEND);
                 PrintWriter pw = new PrintWriter(writerCSV)) {

                String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                pw.println(ucenik.getImeUcenik() + "," + ucenik.getPrezimeUcenik() + "," + currentDateTime);
            }
            catch (Exception e) {

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

	         String csvFilePath = "database/materijali.csv";

	        OidcUser ulogiranKorisnik = (OidcUser) authentification.getPrincipal();
	 		String email = ulogiranKorisnik.getPreferredUsername();

		    Optional<Ucenik> ucenikOptional = ucenikService.findByEmailUcenik(email);


		    Ucenik ucenik = ucenikOptional.get();

	            try (BufferedWriter writerCSV = Files.newBufferedWriter(Paths.get(csvFilePath), StandardOpenOption.APPEND);
	                 PrintWriter pw = new PrintWriter(writerCSV)) {

	                String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	                pw.printf(ucenik.getImeUcenik() + "," + ucenik.getPrezimeUcenik() + "," + currentDateTime + "," + suffix + "," + prefix + "\n");
	            }
	            catch (Exception e) {

				}

	         return ResponseEntity.ok()
	                 .header("Content-Disposition", "attachment; filename=" + suffix)
	                 .contentLength(content.length)
	                 .body(content);
	     } catch (Exception e) {
	         return ResponseEntity.status(500).body(null);
	     }
	 }




    

}

