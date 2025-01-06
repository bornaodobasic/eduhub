package fer.progi.backend.rest;

import fer.progi.backend.service.UcenikService;
import fer.progi.backend.domain.Predmet;

import java.time.LocalDate;
import java.util.List;

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
    

}
