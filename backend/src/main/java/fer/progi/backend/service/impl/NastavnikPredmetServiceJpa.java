package fer.progi.backend.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fer.progi.backend.domain.Aktivnost;
import fer.progi.backend.domain.Ucenik;
import jakarta.transaction.Transactional;
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
	@Transactional
	public boolean dodajPredmetNastavnik(String email, List<String> predmeti) {
		Nastavnik nastavnik = nastavnikRepo.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("Nastavnik nije pronađen s emailom: " + email));
        
		List<Predmet> newPredmets = predmetRepo.findByNazPredmetIn(predmeti);
		
		for(Predmet predmet : newPredmets) {
			predmet.getNastavnici().add(nastavnik);
		}
		
		nastavnik.getPredmeti().addAll(newPredmets);

        return true;
	}

	@Override
	public boolean ukloniPredmetNastavnik(String email, List<String> predmeti) {
		Nastavnik nastavnik = nastavnikRepo.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("Nastavnik nije pronađen s emailom: " + email));

		List<Predmet> predmetiZaUkloniti = predmetRepo.findByNazPredmetIn(predmeti);
		
		nastavnik.getPredmeti().removeAll(predmetiZaUkloniti);
		predmetiZaUkloniti.forEach(p -> p.getNastavnici().remove(nastavnik));

		if (nastavnik.getPredmeti() != null) {
			nastavnik.getPredmeti().removeAll(predmetiZaUkloniti);
			nastavnikRepo.save(nastavnik);

			for(int i = 0; i < predmetiZaUkloniti.size(); i++) {
				predmetiZaUkloniti.get(i).getNastavnici().remove(nastavnik);
				predmetRepo.save(predmetiZaUkloniti.get(i));
			}
			return true;
		}
		else {
			return false;
		}

	}



	@Override
	public Set<Predmet> findNotNastavnikPredmeti(String email) {
		Nastavnik nastavnik = nastavnikRepo.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("Nastavnik nije pronađen s emailom: " + email));

	    Set<Predmet> predmetiNotInNastavnik = new HashSet<>();

	    for(Predmet p : predmetRepo.findAll()) {
			if(!nastavnik.getPredmeti().contains(p)) {
				predmetiNotInNastavnik.add(p);
			}
		}

		return predmetiNotInNastavnik;
	}

}
