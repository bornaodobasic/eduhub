package fer.progi.backend.service.impl;


import fer.progi.backend.dao.UcenikRepository;
import fer.progi.backend.domain.Aktivnost;
import fer.progi.backend.domain.Razred;
import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.domain.Predmet;
import fer.progi.backend.rest.UpisDTO;
import fer.progi.backend.service.AktivnostService;
import fer.progi.backend.service.RazredService;
import fer.progi.backend.service.UcenikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Ucenik ucenik = ucenikRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Učenik nije pronađen s emailom: " + email));

        List<Predmet> listaPredmeta = ucenik.getRazred().getSmjer().getPredmeti();
        
        if(ucenik.getVjeronauk()) {
        	for(Predmet p : listaPredmeta) {
        		if(p.getNazPredmet().startsWith("Etika")) {
        			listaPredmeta.remove(p);
        		}
        	}
        } else if(!ucenik.getVjeronauk()) {
        	for(Predmet p : listaPredmeta) {
        		if(p.getNazPredmet().startsWith("Vjeronauk")) {
        			listaPredmeta.remove(p);
        		}
        	}
        }
        return listaPredmeta;

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
 
}
