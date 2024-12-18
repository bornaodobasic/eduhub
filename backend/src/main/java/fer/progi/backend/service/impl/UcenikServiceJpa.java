package fer.progi.backend.service.impl;

import fer.progi.backend.dao.AktivnostRepository;
import fer.progi.backend.dao.UcenikRepository;
import fer.progi.backend.domain.Admin;
import fer.progi.backend.domain.Aktivnost;
import fer.progi.backend.domain.Razred;
import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.rest.UpisDTO;
import fer.progi.backend.service.RazredService;
import fer.progi.backend.service.UcenikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UcenikServiceJpa implements UcenikService {

    @Autowired
    private UcenikRepository ucenikRepo;

    @Autowired
    private RazredService razredService;
    
    @Autowired
    private AktivnostRepository aktivnostRepo;

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
        ucenikRepo.deleteByEmail(email);
    }
    

    @Override
      public boolean dodajAktivnostiPoNazivu(String email, List<String> naziviAktivnosti) {
         
          Ucenik ucenik = ucenikRepo.findByEmail(email)
                  .orElseThrow(() -> new RuntimeException("Učenik nije pronađen."));

          List<Aktivnost> aktivnosti = aktivnostRepo.findByOznAktivnostIn(naziviAktivnosti);

          if (ucenik.getAktivnosti() == null) {
              ucenik.setAktivnosti(new HashSet<>());
          }
          
          ucenik.getAktivnosti().addAll(aktivnosti);

          ucenikRepo.save(ucenik);

          return true;
      }
    
    @Override
    public Set<Aktivnost> findUcenikAktivnosti(String email) {
        Ucenik ucenik = ucenikRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Učenik nije pronađen s emailom: " + email));
        
        return ucenik.getAktivnosti();
    }

	@Override
	public boolean ukloniAktivnostiPoNazivu(String email, List<String> naziviAktivnosti) {
		
		 Ucenik ucenik = ucenikRepo.findByEmail(email)
		            .orElseThrow(() -> new RuntimeException("Učenik nije pronađen s emailom: " + email));
		 List<Aktivnost> aktivnostiZaUklanjanje = aktivnostRepo.findByOznAktivnostIn(naziviAktivnosti);
		 
		  if (ucenik.getAktivnosti() != null) {
		        ucenik.getAktivnosti().removeAll(aktivnostiZaUklanjanje);
		        ucenikRepo.save(ucenik);
		        return true;
		    }
		  else {
			  return false;
		  }
		    
	
		  

		 
	}


 
}
