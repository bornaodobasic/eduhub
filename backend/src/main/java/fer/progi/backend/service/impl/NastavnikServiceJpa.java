package fer.progi.backend.service.impl;

import java.util.List;
import java.util.Optional;

import fer.progi.backend.domain.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fer.progi.backend.dao.NastavnikRepository;
import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.service.NastavnikService;

@Service
public class NastavnikServiceJpa implements NastavnikService{
	
		
		@Autowired
		private NastavnikRepository nastavnikRepo;

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
		
		
}
