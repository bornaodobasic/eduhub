package fer.progi.backend.rest;

import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.service.AktivnostService;
import fer.progi.backend.service.UcenikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ucenik")
public class UcenikController {

    @Autowired
    private UcenikService ucenikService;

    @Autowired
    private AktivnostService aktivnostService;

    @GetMapping("")
    @Secured("ROLE_GUEST")
    public List<Ucenik> listUcenika() {
        return ucenikService.listAll();
    }

//    @PostMapping("")
//    public Ucenik dodajUcenika(@RequestBody Ucenik ucenik) {
//        return ucenikService.dodajUcenika(ucenik);
//    }

    /*
     * @PostMapping("/{oib}/aktivnosti/{sifAktivnost}") public
     * ResponseEntity<String> dodajAktivnostUceniku(
     *
     * @PathVariable String oib, @PathVariable Integer sifAktivnost) { try {
     * ucenikService.dodajAktivnostUceniku(oib, sifAktivnost); return
     * ResponseEntity.ok("Učeniku uspješno dodana aktivnost"); }
     * catch(RuntimeException e) { return
     * ResponseEntity.status(404).body(e.getMessage()); } }
     */
    
    /*@PostMapping("")
    public ResponseEntity<String> dodajAktivnostUceniku(@RequestBody Ucenik ucenik) {
		try {
			ucenikService.dodajAktivnostUcenikuV2(ucenik);
			return ResponseEntity.ok("Učeniku uspješno dodana aktivnost");
		} catch(RuntimeException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}*/

    @PostMapping("addActivity")
    @Secured("ROLE_ucenik")
    public ResponseEntity<?> dodajAktivnost(@RequestBody Integer sifra) {

        boolean success = ucenikService.addActivity(sifra);
        if (success) {
            return ResponseEntity.ok("Zahtjev uspješno poslan adminu");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Zahtjev nije poslan!");
        }
    }


    }
