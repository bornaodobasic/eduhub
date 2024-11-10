package fer.progi.backend.rest;

import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.service.UcenikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ucenici")
public class UcenikController {

    @Autowired
    private UcenikService ucenikService;

    @GetMapping("")
    public List<Ucenik> listUcenika() {
        return ucenikService.listAll();
    }

    @PostMapping("")
    public Ucenik dodajUcenika(@RequestBody Ucenik ucenik) {
        return ucenikService.dodajUcenika(ucenik);
    }

}
