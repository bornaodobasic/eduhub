package fer.progi.backend.rest;

import fer.progi.backend.service.MailService;
import fer.progi.backend.service.PDFService;
import fer.progi.backend.service.UcenikService;
import fer.progi.backend.domain.Predmet;
import fer.progi.backend.domain.Ucenik;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    
//    @PostMapping("/dodajAktivnosti")
//    public ResponseEntity<String> dodajAktivnosti(Authentication authentication, @RequestBody List<String> oznAktivnosti){
//    	  LocalDate krajnjiRok = LocalDate.of(2025, 9, 1);
//    	  
//    	    if (LocalDate.now().isAfter(krajnjiRok)) {
//    	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
//    	                             .body("Više nije moguće prijaviti aktivnosti jer je prošao rok.");
//    	    }
//        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
//        String email = (String) oidcUser.getAttributes().get("preferred_username");
//        
//        boolean success = ucenikService.dodajAktivnostiPoNazivu(email, oznAktivnosti);
//
//        if (success) {
//            return ResponseEntity.ok("Aktivnosti su uspešno dodane.");
//        } else {
//            return ResponseEntity.badRequest().body("Dodavanje aktivnosti nije uspelo.");
//        }
//    	
//    }

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

        try {
        
            try (BufferedWriter writerCSV = Files.newBufferedWriter(Paths.get(csvFilePath), StandardOpenOption.APPEND);
                 PrintWriter pw = new PrintWriter(writerCSV)) {

                String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                pw.println(ucenik.getImeUcenik() + "," + ucenik.getPrezimeUcenik() + "," + currentDateTime);
            }

        } catch (IOException e) {
           
        }

	    
	    return ResponseEntity.ok()
	            .header("Content-Disposition", "attachment; filename=potvrda.pdf")
	            .header("Content-Type", "application/pdf")
	            .body(pdfBytes);
	    
	}

	 @GetMapping("/{email}/posaljiMail")
	 public ResponseEntity<String> posaljiNaMail(@PathVariable String email){
		 
		    Optional<Ucenik> ucenikOptional = ucenikService.findByEmailUcenik(email);

		    if (ucenikOptional.isEmpty()) {
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); 
		    }

		    Ucenik ucenik = ucenikOptional.get();
		    
		    byte[] pdfBytes = pdfService.generatePDF(ucenik.getImeUcenik(), ucenik.getPrezimeUcenik());
		    
		    mailService.sendMail(email, pdfBytes, "potvrda_" + ucenik.getImeUcenik() + "_" + ucenik.getPrezimeUcenik() + ".pdf" );
		    
		    return ResponseEntity.ok("Mail uspješno poslan");  

		 
	 }

    

}

