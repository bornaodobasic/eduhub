package fer.progi.backend.rest;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.service.impl.UcenikServiceJpa;

@RestController
@RequestMapping("/upis")
public class UpisController {
	
    @Autowired
    private UcenikServiceJpa ucenikServiceJpa;
    
    @GetMapping
    public ResponseEntity<Ucenik> getUpisPage() {
    	OAuth2User principal = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	String email = null;

        // Ako je korisnik uspešno autentifikovan
        if (principal != null) {
            // Dobijamo email iz principal-a
            email = principal.getAttribute("email");
            
        }
        
        System.out.println("provjera 2 " + email);

        Optional<Ucenik> ucenik = ucenikServiceJpa.findByEmail(email);

        if (ucenik.isPresent()) {
            return ResponseEntity.ok(ucenik.get());
        } else {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    // Handles POST requests to update the Ucenik data
    @PostMapping("")
    public ResponseEntity<String> postUpisPage(@ModelAttribute Ucenik ucenik) {
//        // Get the logged-in user's email
//        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        System.out.println("provjera 2 " + loggedInEmail);

    	OAuth2User principal = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	String email = null;

        // Ako je korisnik uspešno autentifikovan
        if (principal != null) {
            // Dobijamo email iz principal-a
            email = principal.getAttribute("email");
            
        }
       
        boolean isUpdated = ucenikServiceJpa.createNewUcenikFirst(
                email,
                ucenik.getImeUcenik(),
                ucenik.getPrezimeUcenik(),
                ucenik.getSpol()
        );

        if (isUpdated) {
            return ResponseEntity.ok("Update successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update failed");
        }
    }
//    
//    @GetMapping("")
//    public String getUpisPage() {
//        return "upis.html"; // Replace with the name of the HTML file to serve the form (e.g., "upis.html").
//    }

//    @PostMapping("")
//    public String upisStudenta(@ModelAttribute Ucenik ucenik) {
//        // Get the logged-in user's email from the SecurityContext
//        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        // Call the service method to update the student information based on the logged-in email
//        ucenikServiceJpa.createNewUcenikFirst(loggedInEmail, ucenik.getImeUcenik(), ucenik.getPrezimeUcenik(), ucenik.getSpol());
//
//        // Return the view name (could be a success page, or redirect to another page)
//        return "redirect:/success"; // Or redirect to another page
//    }
	
//	@Autowired
//    private UcenikServiceJpa ucenikService;
//	
//
//    // Handle GET requests to /upis
//    @GetMapping("")
//    public ResponseEntity<String> getUpisPage() {
//        return ResponseEntity.ok("Upis page is available for POST requests."); // Replace with actual page if needed
//    }
//	
//	@PostMapping("")
//    public ResponseEntity<?> createNewUcenikFirst(@RequestBody RegisterUcenikDTO registerUcenikDTO) {
//		String email = SecurityContextHolder.getContext().getAuthentication().getName();
//    	
//    	boolean success = ucenikService.createNewUcenikFirst(registerUcenikDTO, email);
// 
//        if (success) {
//            return ResponseEntity.ok("Zahtjev uspješno poslan adminu");
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Zahtjev nije poslan!");
//        }
//    }
}
