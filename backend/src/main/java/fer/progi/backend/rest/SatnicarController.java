
package fer.progi.backend.rest;

import java.util.List;

import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fer.progi.backend.domain.Satnicar;


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
    @Autowired
    private UcenikService ucenikService;
    @Autowired
    private NastavnikService nastavnikService;
    @Autowired
    private MailService mailService;

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
        satService.dodijeliRazrednike();

        List<Ucenik> ucenici = ucenikService.findAllUceniks();
        List<Nastavnik> nastavnici = nastavnikService.findAllNastavniks();

        for (Ucenik ucenik : ucenici) {
            if(!ucenik.getEmail().endsWith("eduxhub.onmicrosoft.com")) {
                mailService.RasporedMail(ucenik.getEmail());
            }
        }

        for (Nastavnik nastavnik : nastavnici) {
            if(!nastavnik.getEmail().endsWith("eduxhub.onmicrosoft.com")) {
                mailService.RasporedMail(nastavnik.getEmail());
            }
        }

    }

}