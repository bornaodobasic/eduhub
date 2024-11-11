
package fer.progi.backend.rest;

import java.util.List;
import java.util.Optional;

import fer.progi.backend.domain.TempNastavnik;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import fer.progi.backend.domain.Admin;
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
}
