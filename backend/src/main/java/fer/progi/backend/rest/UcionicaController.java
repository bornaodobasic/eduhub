package fer.progi.backend.rest;

import fer.progi.backend.domain.Ucionica;
import fer.progi.backend.service.UcionicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ucionice")
public class UcionicaController {

    @Autowired
    private UcionicaService ucionicaService;

    @GetMapping("")
    public List<Ucionica> listUcionica() {
        return ucionicaService.listAll();
    }

    @PostMapping("")
    public Ucionica dodajUcionica(@RequestBody Ucionica ucionica) {
        return ucionicaService.dodajUcionica(ucionica);
    }
}
