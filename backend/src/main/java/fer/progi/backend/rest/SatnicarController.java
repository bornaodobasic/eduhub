
package fer.progi.backend.rest;

import java.util.List;

import fer.progi.backend.service.RazredPredmetNastavnikService;
import fer.progi.backend.service.SatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fer.progi.backend.domain.Satnicar;
import fer.progi.backend.service.SatnicarService;



@RestController
@RequestMapping("/api/satnicar")
@PreAuthorize("hasAuthority('Satnicar')")
public class SatnicarController {
	
	@Autowired
	private SatnicarService SatnicarService;

	@Autowired
	private RazredPredmetNastavnikService razredPredmetNastavnikService;

	@Autowired
	private SatService satService;

	@GetMapping("")
	public List<Satnicar> listSatnicar() {
		return SatnicarService.listAll();
	}
	
	@PostMapping("")
	public Satnicar dodajSatnicar(@RequestBody Satnicar satnicar) {
		return SatnicarService.dodajSatnicara(satnicar);
	}

	@GetMapping("/dodijeli")
	public void dodijeli() {
		razredPredmetNastavnikService.dodijeliNastavnike();
	}

	@GetMapping("/raspored")
	public void raspored() {
		satService.generirajRaspored();
	}

}