package fer.progi.backend.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fer.progi.backend.dao.NastavnikRepository;
import fer.progi.backend.dao.PredmetRepository;
import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.domain.Predmet;
import fer.progi.backend.service.NastavnikPredmetService;

@Service
public class NastavnikPredmetServiceJpa implements NastavnikPredmetService{
	
	@Autowired
	private NastavnikRepository nastavnikRepo;

	@Autowired
	private PredmetRepository predmetRepo;
	
	@Override
	public boolean dodajPredmetNastavnik(String email, List<String> predmeti) {
		Nastavnik nastavnik = nastavnikRepo.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("Nastavnik nije pronađen s emailom: " + email));
	        
		if (nastavnik.getPredmeti() == null) {
			nastavnik.setPredmeti(new HashSet<>());
        }
        
		Set<Predmet> newPredmets = new HashSet<>();
		
		for(int i = 0; i < predmeti.size(); i++) {
			Predmet predmet = predmetRepo.findByNazPredmet(predmeti.get(i));
			newPredmets.add(predmet);
			predmet.getNastavnici().add(nastavnik);
			predmetRepo.save(predmet);
			
		}
		
		nastavnik.setPredmeti(newPredmets);
		nastavnikRepo.save(nastavnik);

        return true;
	}

	@Override
	public boolean ukloniPredmetNastavnik(String email, List<String> predmeti) {
		Nastavnik nastavnik = nastavnikRepo.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("Nastavnik nije pronađen s emailom: " + email));
		
		for(int i = 0; i < predmeti.size(); i++) {
			Predmet predmet = predmetRepo.findByNazPredmet(predmeti.get(i));
			if(nastavnik.getPredmeti().contains(predmet)) {
				nastavnik.getPredmeti().remove(predmet);
				predmet.getNastavnici().remove(nastavnik);
				predmetRepo.save(predmet);
			}
		}
		
		nastavnikRepo.save(nastavnik);
		return true;
	}

}
