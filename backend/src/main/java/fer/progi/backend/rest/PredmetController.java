package fer.progi.backend.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fer.progi.backend.domain.Predmet;
import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.service.PredmetService;

@RestController
@RequestMapping("/predmeti")
public class PredmetController {
	
	@Autowired
	private PredmetService predmetService;
	
	@GetMapping("")
	public List<Predmet> listPredmet() {
	return predmetService.listAll();
	}
	
	@PostMapping("")
    public ResponseEntity<String> addPredmet(@RequestBody Predmet predmet) {
        try {
            predmetService.addPredmet(predmet);
            return ResponseEntity.ok("Predmet je uspješno dodan");
        } catch(RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
	
	@PostMapping("/{sifPredmet}/nastavnici/{sifNastavnik}")
	public ResponseEntity<String> dodajNastavnikaUPredmet(
			@PathVariable Integer sifPredmet, @PathVariable Integer sifNastavnik) {
		try {
			predmetService.dodajNastavnikaUPredmet(sifPredmet, sifNastavnik);
			return ResponseEntity.ok("Nastavnik uspješno dodan u predmet");
		} catch(RuntimeException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}
	
}
