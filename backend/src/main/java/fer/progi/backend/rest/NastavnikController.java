package fer.progi.backend.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fer.progi.backend.domain.Predmet;
import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.service.impl.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.service.NastavnikService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/nastavnik")
@PreAuthorize("hasAuthority('Nastavnik')")
public class NastavnikController {

    @Autowired
    private NastavnikService nastavnikService;

    @Autowired
    private S3Service s3Service;


    @PostMapping("")
    public Nastavnik dodajNastavnika(@RequestBody Nastavnik nastavnik) {
        return nastavnikService.dodajNastavnik(nastavnik);
    }

    @GetMapping("/")
    public List<String> getNastavniciMailovi(Authentication authentication) {
    	List<String> mailoviNastavnika = new ArrayList<>();
    	
    	OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
 	    String email = oidcUser.getAttribute("preferred_username");
    	
    	for(Nastavnik n :nastavnikService.findAllNastavniks()) {
    		if(!n.getEmail().equals(email))
    		mailoviNastavnika.add(n.getEmail());
    	}
    	
    	return mailoviNastavnika;
    }

/*
	@GetMapping("/predmeti")
	public List<Predmet> predmeti(@AuthenticationPrincipal OidcUser oidcUser) {
		if (oidcUser == null) {
			throw new IllegalStateException("Korisnik nije autentificiran");
		}

		String email = oidcUser.getPreferredUsername();
		if (email == null || email.isEmpty()) {
			throw new IllegalArgumentException("Email nije pronađen");
		}

		return nastavnikService.findNastavnikPredmeti(email);
	}

*/

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

}
