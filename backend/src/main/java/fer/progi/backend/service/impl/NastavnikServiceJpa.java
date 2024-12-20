package fer.progi.backend.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import fer.progi.backend.domain.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fer.progi.backend.dao.NastavnikRepository;
import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.domain.Predmet;
import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.service.NastavnikService;
import fer.progi.backend.service.PredmetService;

@Service
public class NastavnikServiceJpa implements NastavnikService{
	
		
		@Autowired
		private NastavnikRepository nastavnikRepo;

		
		@Autowired
		private PredmetService predmetService;

		@Override
		public List<Nastavnik> listAll() {
			return nastavnikRepo.findAll();
		}

		@Override
		public Nastavnik dodajNastavnik(Nastavnik nastavnik) {
			return nastavnikRepo.save(nastavnik);
		}

		@Override
		public Nastavnik findBySifNastavnik(Integer sifNastavnik) {
			return nastavnikRepo.findById(sifNastavnik)
					.orElseThrow(() -> new RuntimeException("Nije pronađen nastavnik"));
		}
	public boolean findByEmail(String email) {
		return nastavnikRepo.findByEmail(email).isPresent();
	}

	public Nastavnik getOrCreateNastavnik(String email) {
			return nastavnikRepo.findByEmail(email)
					.orElseGet(() -> createNewNastavnik(email));
	}

	private Nastavnik createNewNastavnik(String email) {
		Nastavnik nastavnik = new Nastavnik();
		nastavnik.setEmail(email);
		return nastavnikRepo.save(nastavnik);
	}

	@Override
	public boolean createIfNeeded(String email) {
		Optional<Nastavnik> optionalNastavnik = nastavnikRepo.findByEmail(email);
		if (optionalNastavnik.isEmpty()) {
			Nastavnik nastavnik = new Nastavnik();
			nastavnik.setEmail(email);
			nastavnikRepo.save(nastavnik);
		}
		return true;
	}

	@Override
	public List<Nastavnik> findAllNastavniks() {
		return nastavnikRepo.findAll();
	}

	@Override
	public void deleteNastavnik(String email) {
		nastavnikRepo.deleteByEmail(email);
	}

	@Override
	public Set<Predmet> findNastavnikPredmeti(String email) {
		Nastavnik nastavnik = nastavnikRepo.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("Nastavnik nije pronađen s emailom: " + email));
	        
	        return nastavnik.getPredmeti();
	}

	@Override
	public boolean dodajPredmetNastavniku(String email, List<String> predmeti) {
		Nastavnik nastavnik = nastavnikRepo.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("Nastavnik nije pronađen s emailom: " + email));
	        
		if (nastavnik.getPredmeti() == null) {
			nastavnik.setPredmeti(new HashSet<>());
        }
        
		Set<Predmet> newPredmets = new HashSet<>();
		
		for(int i = 0; i < predmeti.size(); i++) {
			Predmet predmet = predmetService.findPredmetByNaziv(predmeti.get(i));
			newPredmets.add(predmet);
			
		}
		
		nastavnik.setPredmeti(newPredmets);
		nastavnikRepo.save(nastavnik);

        return true;
	}

	@Override
	public boolean ukloniPredmetNastavniku(String email, List<String> predmeti) {
		Nastavnik nastavnik = nastavnikRepo.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("Nastavnik nije pronađen s emailom: " + email));
		
		for(int i = 0; i < predmeti.size(); i++) {
			Predmet predmet = predmetService.findPredmetByNaziv(predmeti.get(i));
			if(nastavnik.getPredmeti().contains(predmet)) {
				nastavnik.getPredmeti().remove(predmet);
			}
		}
		
		nastavnikRepo.save(nastavnik);
		return true;
	}
		
}
