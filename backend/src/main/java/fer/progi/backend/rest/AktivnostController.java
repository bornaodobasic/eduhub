package fer.progi.backend.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fer.progi.backend.domain.Aktivnost;
import fer.progi.backend.service.AktivnostService;

@RestController
@RequestMapping("/aktivnosti")
public class AktivnostController {
	
	@Autowired
	private AktivnostService aktivnostService;
	
	@GetMapping("")
	public List<Aktivnost> listAktivnost() {
	return aktivnostService.listAll();
	}
	
	@PostMapping("")
    public Aktivnost dodajAktivnost(@RequestBody Aktivnost aktivnost) {
        return aktivnostService.dodajAktivnost(aktivnost);
    }

}
