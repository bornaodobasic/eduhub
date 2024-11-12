
package fer.progi.backend.rest;

import java.util.List;
import java.util.Optional;

import fer.progi.backend.domain.TempNastavnik;
import fer.progi.backend.domain.TempRavnatelj;
import fer.progi.backend.domain.TempSatnicar;
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
	
	//Ravnatelj------------------------------------------------------------------------------------------------------------------------------------------

	@GetMapping("/tempRavnatelj")
	@Secured("ROLE_administrator")
	public ResponseEntity<List<TempRavnatelj>> dohvatiRavnatelja() {
		return ResponseEntity.ok(AdminService.dohvatiSveZahtjeveRavnatelja());
	}
	
	@PostMapping("/tempRavnatelj/{email}/odobri")
	@Secured("ROLE_administrator")
	public ResponseEntity<?> odobriRavnatelja(@PathVariable String email) {
		Optional<TempRavnatelj> tempRavnatelj = AdminService.dohvatiZahtjevRavnateljaPoId(email);

		if (tempRavnatelj.isPresent()) {
			AdminService.odobriRavnatelja(tempRavnatelj.get());
			return ResponseEntity.ok("Uspješno pretvoren u ravnatelja");
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zahtjev nije pronađen");
	}
	
	@DeleteMapping("/tempRavnatelj/{email}")
	@Secured("ROLE_administrator")
	public ResponseEntity<?> odbaciRavnatelja(@PathVariable String email) {
		boolean deleted = AdminService.odbaciRavnatelja(email);
		if (deleted) {
			return ResponseEntity.ok("Zahtjev uspješno odbačen.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zahtjev nije pronađen");
	}

	//Satnicar-----------------------------------------------------------------------------------------------------------------------------------------------
	
	@GetMapping("/tempSatnicar")
	@Secured("ROLE_administrator")
	public ResponseEntity<List<TempSatnicar>> dohvatiSatnicara() {
		return ResponseEntity.ok(AdminService.dohvatiSveZahtjeveSatnicara());
	}

	@PostMapping("/tempSatnicar/{email}/odobri")
	@Secured("ROLE_administrator")
	public ResponseEntity<?> odobriSatnicara(@PathVariable String email) {
		Optional<TempSatnicar> tempSatnicar = AdminService.dohvatiZahtjevSatnicaraPoId(email);

		if (tempSatnicar.isPresent()) {
			AdminService.odobriSatnicara(tempSatnicar.get());
			return ResponseEntity.ok("Uspješno pretvoren u satnicara");
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zahtjev nije pronađen");
	}


	@DeleteMapping("/tempSatnicar/{email}")
	@Secured("ROLE_administrator")
	public ResponseEntity<?> odbaciSatnicara(@PathVariable String email) {
		boolean deleted = AdminService.odbaciSatnicara(email);
		if (deleted) {
			return ResponseEntity.ok("Zahtjev uspješno odbačen.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zahtjev nije pronađen");
	}

}


