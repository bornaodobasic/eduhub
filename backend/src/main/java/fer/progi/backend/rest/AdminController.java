
package fer.progi.backend.rest;

import java.util.List;
import java.util.Optional;

import fer.progi.backend.domain.TempNastavnik;
import fer.progi.backend.domain.TempUcenik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import fer.progi.backend.domain.Admin;
import fer.progi.backend.domain.TempDjelatnik;
import fer.progi.backend.service.AdminService;


@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private AdminService AdminService;
	
	@GetMapping("")
	public List<Admin> listAdmin() {
		return AdminService.listAll();
	}
	
	@PostMapping("")
	public Admin dodajAdmin(@RequestBody Admin admin) {
		return AdminService.dodajAdmin(admin);
	}


	
	//Nastavnik------------------------------------------------------------------------------------------------------------------------------
	
	@GetMapping("/tempNastavnik")
	@Secured("ROLE_administrator")
	public ResponseEntity<List<TempNastavnik>> dohvatiNastavnike() {
		return ResponseEntity.ok(AdminService.dohvatiSveZahtjeveNastavnika());
	}

	@PostMapping("/tempNastavnik/{email}/odobri")
	@Secured("ROLE_administrator")
	public ResponseEntity<?> odobriNastavnika(@PathVariable String email) {
		Optional<TempNastavnik> tempNastavnik = AdminService.dohvatiZahtjevNastavnikaPoId(email);

		if (tempNastavnik.isPresent()) {
			AdminService.odobriNastavnika(tempNastavnik.get());
			return ResponseEntity.ok("Uspješno pretvoren u nastavnika");
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zahtjev nije pronađen");
	}

	@DeleteMapping("/tempNastavnik/{email}")
	@Secured("ROLE_administrator")
	public ResponseEntity<?> odbaciNastavnika(@PathVariable String email) {
		boolean deleted = AdminService.odbaciNastavnika(email);
		if (deleted) {
			return ResponseEntity.ok("Zahtjev uspješno odbačen.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zahtjev nije pronađen");
	}

//Ucenik------------------------------------------------------------------------------------------------------------------------------------------

@GetMapping("/tempUcenik")
@Secured("ROLE_administrator")
public ResponseEntity<List<TempUcenik>> dohvatiUcenike() {
	return ResponseEntity.ok(AdminService.dohvatiSveZahtjeveUcenika());
}

@PostMapping("/tempUcenik/{email}/odobri")
@Secured("ROLE_administrator")
public ResponseEntity<?> odobriUcenika(@PathVariable String email) {
	Optional<TempUcenik> tempUcenik = AdminService.dohvatiZahtjevUcenikaPoId(email);

	if (tempUcenik.isPresent()) {
		AdminService.odobriUcenika(tempUcenik.get());
		return ResponseEntity.ok("Uspješno pretvoren u Ucenika");
	}

	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zahtjev nije pronađen");
}

@DeleteMapping("/tempUcenik/{email}")
@Secured("ROLE_administrator")
public ResponseEntity<?> odbaciUcenika(@PathVariable String email) {
	boolean deleted = AdminService.odbaciUcenika(email);
	if (deleted) {
		return ResponseEntity.ok("Zahtjev uspješno odbačen.");
	}
	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zahtjev nije pronađen");
}


//Djelatnik---------------------------------------------------------------------------------------------------------------------------------------
@GetMapping("/tempDjelatnik")
@Secured("ROLE_administrator")
public ResponseEntity<List<TempDjelatnik>> dohvatiDjelatnike() {
	return ResponseEntity.ok(AdminService.dohvatiSveZahtjeveDjelatnika());
}

@PostMapping("/tempDjelatnik/{email}/odobri")
@Secured("ROLE_administrator")
public ResponseEntity<?> odobriDjelatnika(@PathVariable String email) {
	Optional<TempDjelatnik> tempDjelatnik = AdminService.dohvatiZahtjevDjelatnikaPoId(email);

	if (tempDjelatnik.isPresent()) {
		AdminService.odobriDjelatnika(tempDjelatnik.get());
		return ResponseEntity.ok("Uspješno pretvoren u Djelatnika");
	}

	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zahtjev nije pronađen");
}

@DeleteMapping("/tempDjelatnik/{email}")
@Secured("ROLE_administrator")
public ResponseEntity<?> odbaciDjelatnika(@PathVariable String email) {
	boolean deleted = AdminService.odbaciDjelatnika(email);
	if (deleted) {
		return ResponseEntity.ok("Zahtjev uspješno odbačen.");
	}
	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zahtjev nije pronađen");
}
}


