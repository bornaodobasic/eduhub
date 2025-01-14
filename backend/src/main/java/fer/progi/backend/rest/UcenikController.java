package fer.progi.backend.rest;

import fer.progi.backend.service.MailService;
import fer.progi.backend.service.PDFService;
import fer.progi.backend.service.UcenikService;
import fer.progi.backend.service.impl.S3Service;
import fer.progi.backend.domain.Predmet;
import fer.progi.backend.domain.Ucenik;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
    
    @Autowired S3Service s3Service;
    


	@GetMapping("/predmeti") 
	public ResponseEntity<List<Predmet>> dohvatiSveupisanePredmete(Authentication authentification){
		
		
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

	    
	        String csvFileKey = "zahtjevi/zahtjevi.csv";

	       
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
	            String newLine = ucenik.getImeUcenik() + "," + ucenik.getPrezimeUcenik() + "," + currentDateTime;
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
	 public ResponseEntity<String> posaljiNaMail(@PathVariable String email){
		 
		    Optional<Ucenik> ucenikOptional = ucenikService.findByEmailUcenik(email);

		    if (ucenikOptional.isEmpty()) {
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); 
		    }

		    Ucenik ucenik = ucenikOptional.get();
		    
		    byte[] pdfBytes = pdfService.generatePDF(ucenik.getImeUcenik(), ucenik.getPrezimeUcenik());
		    
		    mailService.sendMail(email, pdfBytes, "potvrda_" + ucenik.getImeUcenik() + "_" + ucenik.getPrezimeUcenik() + ".pdf" );
		    

	        String csvFileKey = "zahtjevi/zahtjevi.csv";

	       
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
	            String newLine = ucenik.getImeUcenik() + "," + ucenik.getPrezimeUcenik() + "," + currentDateTime;
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

	         
	         String csvFileKey = "materijali/materijali.csv";
	         Path tempFilePath = null;

	         try {
	             
	             tempFilePath = Files.createTempFile("temp-materijali", ".csv");
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
	             String newLine = ucenik.getImeUcenik() + "," + ucenik.getPrezimeUcenik() + "," + currentDateTime + "," + suffix + "," + prefix;
	             Files.write(tempFilePath, (newLine + "\n").getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);

	             s3Service.uploadFileFromPath(csvFileKey, tempFilePath);

	         } catch (Exception e) {
	             e.printStackTrace();
	             return ResponseEntity.status(500).body(null); 
	         } finally {
	           
	             if (tempFilePath != null) {
	                 try {
	                     Files.delete(tempFilePath);
	                 } catch (IOException e) {
	                     System.err.println("Nije moguće obrisati privremenu datoteku: " + tempFilePath);
	                 }
	             }
	         }

	        
	         return ResponseEntity.ok()
	                 .header("Content-Disposition", "attachment; filename=" + fileName)
	                 .contentLength(content.length)
	                 .body(content);

	     } catch (Exception e) {
	         e.printStackTrace();
	         return ResponseEntity.status(500).body(null); 
	     }
	 }



    

}

