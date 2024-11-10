package fer.progi.backend.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fer.progi.backend.domain.Smjer;
import fer.progi.backend.service.SmjerService;

@RestController
@RequestMapping("/smjerovi")
public class SmjerController {
	
	@Autowired
	private SmjerService smjerService;
	
	@GetMapping("")
	public List<Smjer> listSmjer() {
	return smjerService.listAll();
	}
	
	@PostMapping("")
    public Smjer dodajSmjer(@RequestBody Smjer smjer) {
        return smjerService.dodajSmjer(smjer);
    }

}