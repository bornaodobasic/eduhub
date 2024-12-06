
package fer.progi.backend.rest;

import java.util.List;
import java.util.Optional;

import fer.progi.backend.domain.TempNastavnik;
import fer.progi.backend.domain.TempRavnatelj;
import fer.progi.backend.domain.TempSatnicar;
import fer.progi.backend.domain.TempUcenik;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import fer.progi.backend.domain.Admin;
import fer.progi.backend.domain.TempAdmin;
import fer.progi.backend.domain.TempDjelatnik;
import fer.progi.backend.service.AdminService;


@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('Admin')")
public class AdminController {
	
	@Autowired
	private AdminService AdminService;
	
	@PostMapping("/add")
	public Admin dodajAdmin(@RequestBody Admin admin) {
		return AdminService.dodajAdmin(admin);
	}
	
	// Admin------------------------------------------------------------------------------------------------------------------------------


	@GetMapping("/zahtjevi/tempAdmin")
	public ResponseEntity<List<TempAdmin>> dohvatiAdmine() {
	    return ResponseEntity.ok(AdminService.dohvatiSveZahtjeveAdmina());
	}

	@PostMapping("/zahtjevi/tempAdmin/{email}/odobri")
	public ResponseEntity<?> odobriAdmina(@PathVariable String email) {
	    Optional<TempAdmin> tempAdmin = AdminService.dohvatiZahtjevAdminaPoId(email);

	    if (tempAdmin.isPresent()) {
	        AdminService.odobriAdmina(tempAdmin.get());
	        return ResponseEntity.ok("Uspješno pretvoren u admina");
	    }

	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zahtjev nije pronađen");
	}

	@DeleteMapping("/zahtjevi/tempAdmin/{email}")
	@Secured("ROLE_administrator")
	public ResponseEntity<?> odbaciAdmina(@PathVariable String email) {
	    boolean deleted = AdminService.odbaciAdmina(email);
	    if (deleted) {
	        return ResponseEntity.ok("Zahtjev uspješno odbačen.");
	    }
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zahtjev nije pronađen");
	}
	
	
	//Nastavnik------------------------------------------------------------------------------------------------------------------------------
	
	@GetMapping("/zahtjevi/tempNastavnik")
	public ResponseEntity<List<TempNastavnik>> dohvatiNastavnike() {
		return ResponseEntity.ok(AdminService.dohvatiSveZahtjeveNastavnika());
	}

	@PostMapping("/zahtjevi/tempNastavnik/{email}/odobri")
	public ResponseEntity<?> odobriNastavnika(@PathVariable String email) {
		Optional<TempNastavnik> tempNastavnik = AdminService.dohvatiZahtjevNastavnikaPoId(email);

		if (tempNastavnik.isPresent()) {
			AdminService.odobriNastavnika(tempNastavnik.get());
			return ResponseEntity.ok("Uspješno pretvoren u nastavnika");
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zahtjev nije pronađen");
	}

	@DeleteMapping("/zahtjevi/tempNastavnik/{email}")
	@Secured("ROLE_administrator")
	public ResponseEntity<?> odbaciNastavnika(@PathVariable String email) {
		boolean deleted = AdminService.odbaciNastavnika(email);
		if (deleted) {
			return ResponseEntity.ok("Zahtjev uspješno odbačen.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zahtjev nije pronađen");
	}

	//Ucenik------------------------------------------------------------------------------------------------------------------------------------------

	@GetMapping("/zahtjevi/tempUcenik")
	@Secured("ROLE_administrator")
	public ResponseEntity<List<TempUcenik>> dohvatiUcenike() {
		return ResponseEntity.ok(AdminService.dohvatiSveZahtjeveUcenika());
	}

	@PostMapping("/zahtjevi/tempUcenik/{email}/odobri")
	@Secured("ROLE_administrator")
	public ResponseEntity<?> odobriUcenika(@PathVariable String email) {
		Optional<TempUcenik> tempUcenik = AdminService.dohvatiZahtjevUcenikaPoId(email);

		if (tempUcenik.isPresent()) {
			AdminService.odobriUcenika(tempUcenik.get());
			return ResponseEntity.ok("Uspješno pretvoren u Ucenika");
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zahtjev nije pronađen");
	}

	@DeleteMapping("/zahtjevi/tempUcenik/{email}")
	@Secured("ROLE_administrator")
	public ResponseEntity<?> odbaciUcenika(@PathVariable String email) {
		boolean deleted = AdminService.odbaciUcenika(email);
		if (deleted) {
			return ResponseEntity.ok("Zahtjev uspješno odbačen.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zahtjev nije pronađen");
	}


	//Djelatnik---------------------------------------------------------------------------------------------------------------------------------------
	
	@GetMapping("/zahtjevi/tempDjelatnik")
	@Secured("ROLE_administrator")
	public ResponseEntity<List<TempDjelatnik>> dohvatiDjelatnike() {
		return ResponseEntity.ok(AdminService.dohvatiSveZahtjeveDjelatnika());
	}

	@PostMapping("/zahtjevi/tempDjelatnik/{email}/odobri")
	@Secured("ROLE_administrator")
	public ResponseEntity<?> odobriDjelatnika(@PathVariable String email) {
		Optional<TempDjelatnik> tempDjelatnik = AdminService.dohvatiZahtjevDjelatnikaPoId(email);

		if (tempDjelatnik.isPresent()) {
			AdminService.odobriDjelatnika(tempDjelatnik.get());
			return ResponseEntity.ok("Uspješno pretvoren u Djelatnika");
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zahtjev nije pronađen");
	}

	@DeleteMapping("/zahtjevi/tempDjelatnik/{email}")
	@Secured("ROLE_administrator")
	public ResponseEntity<?> odbaciDjelatnika(@PathVariable String email) {
		boolean deleted = AdminService.odbaciDjelatnika(email);
		if (deleted) {
			return ResponseEntity.ok("Zahtjev uspješno odbačen.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zahtjev nije pronađen");
	}
	
	//Ravnatelj------------------------------------------------------------------------------------------------------------------------------------------

	@GetMapping("/zahtjevi/tempRavnatelj")
	@Secured("ROLE_administrator")
	public ResponseEntity<List<TempRavnatelj>> dohvatiRavnatelja() {
		return ResponseEntity.ok(AdminService.dohvatiSveZahtjeveRavnatelja());
	}
	
	@PostMapping("/zahtjevi/tempRavnatelj/{email}/odobri")
	@Secured("ROLE_administrator")
	public ResponseEntity<?> odobriRavnatelja(@PathVariable String email) {
		Optional<TempRavnatelj> tempRavnatelj = AdminService.dohvatiZahtjevRavnateljaPoId(email);

		if (tempRavnatelj.isPresent()) {
			AdminService.odobriRavnatelja(tempRavnatelj.get());
			return ResponseEntity.ok("Uspješno pretvoren u ravnatelja");
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zahtjev nije pronađen");
	}
	
	@DeleteMapping("/zahtjevi/tempRavnatelj/{email}")
	@Secured("ROLE_administrator")
	public ResponseEntity<?> odbaciRavnatelja(@PathVariable String email) {
		boolean deleted = AdminService.odbaciRavnatelja(email);
		if (deleted) {
			return ResponseEntity.ok("Zahtjev uspješno odbačen.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zahtjev nije pronađen");
	}

	//Satnicar-----------------------------------------------------------------------------------------------------------------------------------------------
	
	@GetMapping("/zahtjevi/tempSatnicar")
	@Secured("ROLE_administrator")
	public ResponseEntity<List<TempSatnicar>> dohvatiSatnicara() {
		return ResponseEntity.ok(AdminService.dohvatiSveZahtjeveSatnicara());
	}

	@PostMapping("/zahtjevi/tempSatnicar/{email}/odobri")
	@Secured("ROLE_administrator")
	public ResponseEntity<?> odobriSatnicara(@PathVariable String email) {
		Optional<TempSatnicar> tempSatnicar = AdminService.dohvatiZahtjevSatnicaraPoId(email);

		if (tempSatnicar.isPresent()) {
			AdminService.odobriSatnicara(tempSatnicar.get());
			return ResponseEntity.ok("Uspješno pretvoren u satnicara");
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zahtjev nije pronađen");
	}


	@DeleteMapping("/zahtjevi/tempSatnicar/{email}")
	@Secured("ROLE_administrator")
	public ResponseEntity<?> odbaciSatnicara(@PathVariable String email) {
		boolean deleted = AdminService.odbaciSatnicara(email);
		if (deleted) {
			return ResponseEntity.ok("Zahtjev uspješno odbačen.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zahtjev nije pronađen");
	}

}


