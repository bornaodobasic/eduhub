package fer.progi.backend.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fer.progi.backend.domain.Razred;
import fer.progi.backend.service.RazredService;

@RestController
@RequestMapping("/razredi")
public class RazredController {

    @Autowired
    private RazredService razredService;

    @GetMapping("")
    public List<Razred> listRazred() {
        return razredService.listAll();
    }
    
}
