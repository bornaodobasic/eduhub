package fer.progi.backend.rest;

import java.util.List;
import fer.progi.backend.domain.Predmet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.service.NastavnikService;

@RestController
@RequestMapping("/api/nastavnik")
@PreAuthorize("hasAuthority('Nastavnik')")
public class NastavnikController {
	
	@Autowired
	private NastavnikService nastavnikService;

	
	@PostMapping("")
	public Nastavnik dodajNastavnika(@RequestBody Nastavnik nastavnik) {
		return nastavnikService.dodajNastavnik(nastavnik);
	}

	@GetMapping("/predmeti")
	public List<Predmet> predmeti(String email) {
		return nastavnikService.findNastavnikPredmeti(email);
	}

}
