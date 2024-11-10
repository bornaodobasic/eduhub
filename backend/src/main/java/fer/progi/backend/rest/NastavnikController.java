package fer.progi.backend.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.service.NastavnikService;

@RestController
@RequestMapping("/nastavnici")
public class NastavnikController {
	
	@Autowired
	private NastavnikService nastavnikService;
	
	@GetMapping("")
	public List<Nastavnik> listNastavnik() {
		return nastavnikService.listAll();
	}
	
	@PostMapping("")
	public Nastavnik dodajNastavnika(@RequestBody Nastavnik nastavnik) {
		return nastavnikService.dodajNastavnik(nastavnik);
	}

}
