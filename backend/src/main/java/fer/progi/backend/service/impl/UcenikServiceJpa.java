package fer.progi.backend.service.impl;


import fer.progi.backend.dao.UcenikRepository;
import fer.progi.backend.domain.*;
import fer.progi.backend.rest.RasporedDTO;
import fer.progi.backend.rest.UpisDTO;
import fer.progi.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class UcenikServiceJpa implements UcenikService {

    @Autowired
    private UcenikRepository ucenikRepo;

    @Autowired
    private RazredService razredService;

    @Autowired
    private AktivnostService aktivnostService;

    @Autowired
    private RazredPredmetNastavnikService rpnService;

    @Autowired
    private SatService satService;

    @Override
    public boolean findByEmail(String email) {
        return ucenikRepo.findByEmail(email).isPresent();
    }

    @Override
    public boolean createNewUcenik(String email, UpisDTO upisDTO) {
        Ucenik ucenik = new Ucenik();

        ucenik.setImeUcenik(upisDTO.getImeUcenik());
        ucenik.setPrezimeUcenik(upisDTO.getPrezimeUcenik());
        ucenik.setSpol(upisDTO.getSpol());
        ucenik.setDatumRodenja(upisDTO.getDatumRodenja());
        ucenik.setOib(upisDTO.getOib());
        ucenik.setEmail(email);
        ucenik.setVjeronauk(upisDTO.getVjeronauk());

        Razred razred = razredService.getBestClass(upisDTO.getSmjer());
        ucenik.setRazred(razred);

        ucenikRepo.save(ucenik);

        return true;
    }

    @Override
    public List<Ucenik> findAllUceniks() {
        return ucenikRepo.findAll();
    }

    @Override
    public void deleteUcenik(String email) {
        Ucenik ucenik = ucenikRepo.findByEmail(email).orElse(null);
        ucenikRepo.delete(ucenik);
    }
    
    @Override
    public List<Aktivnost> findUcenikAktivnosti(String email) {
        Ucenik ucenik = ucenikRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Učenik nije pronađen s emailom: " + email));
        
        return ucenik.getAktivnosti();
    }

    @Override
    public List<Predmet> listAllPredmeti(String email) {
        Ucenik ucenik = ucenikRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Učenik nije pronađen s emailom: " + email));

        List<Predmet> predmeti = ucenik.getRazred().getSmjer().getPredmeti();


        if(ucenik.getVjeronauk()) {
            predmeti.removeIf(p -> p.getNazPredmet().startsWith("Etika"));
        } else {
            predmeti.removeIf(p -> p.getNazPredmet().startsWith("Vjeronauk"));
        }

        return predmeti;
    }

	@Override
	public Optional<Ucenik> findByEmailUcenik(String email) {
		return ucenikRepo.findByEmail(email);
	
	}

	@Override
	public boolean dodajAktivnostiPoNazivu(String email, List<String> oznAktivnosti) {

		Ucenik ucenik = ucenikRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Učenik nije pronađen s emailom: " + email));
		List<Aktivnost> listaAktivnosti = aktivnostService.findByOznAktivnosti(oznAktivnosti);
		ucenik.setAktivnosti(listaAktivnosti);

		return true;

	}

    @Override
    public List<RasporedDTO> getRaspored(String email) {
        Ucenik ucenik = ucenikRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Učenik nije pronađen s emailom: " + email));
        Razred razred = ucenik.getRazred();
        List<Predmet> predmeti = listAllPredmeti(email);
        List<RazredPredmetNastavnik> rpnLista = new ArrayList<>();

        for(Predmet p : predmeti) {
            rpnLista.add(rpnService.findRP(razred, p));
        }
        List<Sat> satiUcenika = satService.listAll().stream().filter(sat -> rpnLista.contains(sat.getRpn())).toList();
        List<RasporedDTO> raspored = new ArrayList<>();

        for(Sat sat : satiUcenika) {
            RasporedDTO rasporedDTO = new RasporedDTO();
            rasporedDTO.setDan(sat.getVrijemeSata().getDan());
            rasporedDTO.setPredmet(sat.getRpn().getPredmet().getNazPredmet());
            rasporedDTO.setUcionica(sat.getUcionica().getOznakaUc());
            rasporedDTO.setNastavnik(sat.getRpn().getNastavnik().getImeNastavnik() + " " + sat.getRpn().getNastavnik().getPrezimeNastavnik());
            rasporedDTO.setKrajSata(sat.getVrijemeSata().getKrajSata());
            rasporedDTO.setPocetakSata(sat.getVrijemeSata().getPocetakSata());
            raspored.add(rasporedDTO);
        }
        return raspored.stream().sorted(Comparator.comparing(RasporedDTO::getDan)).toList();
    }

}
