
package fer.progi.backend.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fer.progi.backend.domain.Ravnatelj;
import fer.progi.backend.service.RavnateljService;


@RestController
@RequestMapping("/ravnatelj")
@PreAuthorize("hasAuthority('Ravnatelj')")
public class RavnateljController {
	
	@Autowired
	private RavnateljService RavnateljService;
	
	@GetMapping("")
	public List<Ravnatelj> listRavnatelj() {
		return RavnateljService.listAll();
	}
	
	@PostMapping("")
	public Ravnatelj dodajRavnatelja(@RequestBody Ravnatelj ravnatelj) {
		return RavnateljService.dodajRavnatelj(ravnatelj);
	}
	
}

