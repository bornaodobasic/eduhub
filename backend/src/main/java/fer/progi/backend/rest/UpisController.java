package fer.progi.backend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fer.progi.backend.service.UcenikService;

@RestController
@RequestMapping("/upis")
public class UpisController {
	
	@Autowired
    private UcenikServiceJpa ucenikService;
	
	@PostMapping("")
    public ResponseEntity<?> createNewUcenik(@RequestBody RegisterUcenikDTO registerUcenikDTO) {
    	
    	boolean success = ucenikService.createNewUcenik(registerUcenikDTO);
 
        if (success) {
            return ResponseEntity.ok("Zahtjev uspješno poslan adminu");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Zahtjev nije poslan!");
        }
    }
}
