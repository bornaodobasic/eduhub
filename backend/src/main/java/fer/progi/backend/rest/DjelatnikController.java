
package fer.progi.backend.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fer.progi.backend.domain.Djelatnik;
import fer.progi.backend.service.DjelatnikService;



@RestController
@RequestMapping("/djelatnici")
public class DjelatnikController {
	
	@Autowired
	private DjelatnikService DjelatnikService;
	
	@GetMapping("")
	public List<Djelatnik> listDjelatnik() {
		return DjelatnikService.listAll();
	}
	
	@PostMapping("")
	public Djelatnik dodajDjelatnik(@RequestBody Djelatnik djelatnik) {
		return DjelatnikService.dodajDjelatnik(djelatnik);
	}

}
