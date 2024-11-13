
package fer.progi.backend.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fer.progi.backend.domain.Satnicar;
import fer.progi.backend.service.SatnicarService;



@RestController
@RequestMapping("/satnicari")
public class SatnicarController {
	
	@Autowired
	private SatnicarService SatnicarService;
	
	@GetMapping("")
	public List<Satnicar> listSatnicar() {
		return SatnicarService.listAll();
	}
	
	@PostMapping("")
	public Satnicar dodajSatnicar(@RequestBody Satnicar satnicar) {
		return SatnicarService.dodajSatnicara(satnicar);
	}

}