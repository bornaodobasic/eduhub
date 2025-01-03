package fer.progi.backend.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fer.progi.backend.dao.AktivnostRepository;
import fer.progi.backend.dao.UcenikRepository;
import fer.progi.backend.domain.Aktivnost;
import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.service.UcenikAktivnostService;

@Service
public class UcenikAktivnostServiceJpa implements UcenikAktivnostService{
	
	@Autowired
	private UcenikRepository ucenikRepo;

	@Autowired
	private AktivnostRepository aktivnostRepo;
	

    @Override
      public boolean dodajUcenikAktivnost(String email, List<String> naziviAktivnosti) {
         
          Ucenik ucenik = ucenikRepo.findByEmail(email)
                  .orElseThrow(() -> new RuntimeException("Učenik nije pronađen."));

          List<Aktivnost> aktivnosti = aktivnostRepo.findByOznAktivnostIn(naziviAktivnosti);

          if (ucenik.getAktivnosti() == null) {
              ucenik.setAktivnosti(new HashSet<>());
          }
          
          for(int i = 0; i < aktivnosti.size(); i++) {
        	  aktivnosti.get(i).getUcenici().add(ucenik);
        	  aktivnostRepo.save(aktivnosti.get(i));
          }
          
          ucenik.getAktivnosti().addAll(aktivnosti);

          ucenikRepo.save(ucenik);

          return true;
      }

	@Override
	public boolean ukloniUcenikAktivnost(String email, List<String> naziviAktivnosti) {
		
		 Ucenik ucenik = ucenikRepo.findByEmail(email)
		            .orElseThrow(() -> new RuntimeException("Učenik nije pronađen s emailom: " + email));
		 List<Aktivnost> aktivnostiZaUklanjanje = aktivnostRepo.findByOznAktivnostIn(naziviAktivnosti);
		 
		  if (ucenik.getAktivnosti() != null) {
		        ucenik.getAktivnosti().removeAll(aktivnostiZaUklanjanje);
		        ucenikRepo.save(ucenik);
		        
		          for(int i = 0; i < aktivnostiZaUklanjanje.size(); i++) {
		        	  aktivnostiZaUklanjanje.get(i).getUcenici().remove(ucenik);
		        	  aktivnostRepo.save(aktivnostiZaUklanjanje.get(i));
		          }
		        return true;
		    }
		  else {
			  return false;
		  }

		 
	}

	@Override
	public Set<Aktivnost> findNotUcenikAktivnosti(String email) {
		Ucenik ucenik = ucenikRepo.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("Nije pronađen učenik s emailom: " + email));

	    Set<Aktivnost> aktivnostNotInUcenik = new HashSet<>();

	    for(Aktivnost a : aktivnostRepo.findAll()) {
			if(!ucenik.getAktivnosti().contains(a)) {
				aktivnostNotInUcenik.add(a);
			}
		}

		return aktivnostNotInUcenik;
	}

}
