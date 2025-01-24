package fer.progi.backend.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import fer.progi.backend.rest.AddDTO;
import fer.progi.backend.rest.RasporedDTO;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fer.progi.backend.dao.NastavnikRepository;
import fer.progi.backend.dao.*;
import fer.progi.backend.domain.*;
import fer.progi.backend.domain.Predmet;
import fer.progi.backend.domain.RazredPredmetNastavnik;
import fer.progi.backend.service.NastavnikService;
import fer.progi.backend.service.*;

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
		
	@Override
	public Optional<Nastavnik> findByEmail(String email) {
		return nastavnikRepo.findByEmail(email);
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
	public boolean createIfNeeded(AddDTO addDTO) {
		Optional<Nastavnik> optionalNastavnik = nastavnikRepo.findByEmail(addDTO.getEmail());
		if (optionalNastavnik.isEmpty()) {
			Nastavnik nastavnik = new Nastavnik();
			nastavnik.setEmail(addDTO.getEmail());
			nastavnik.setImeNastavnik(addDTO.getIme());
			nastavnik.setPrezimeNastavnik(addDTO.getPrezime());
			nastavnikRepo.save(nastavnik);
		}
		return true;
	}

	@Override
	public List<Nastavnik> findAllNastavniks() {
		return nastavnikRepo.findAll();
	}

	@Override
	public boolean deleteNastavnik(String email) {
		Nastavnik nastavnik = nastavnikRepo.findByEmail(email).orElse(null);
		if (nastavnik != null && (nastavnik.getPredmeti() == null || nastavnik.getPredmeti().isEmpty())) {
			nastavnikRepo.delete(nastavnik);
			return true;
		}
		return false;
	}


	@Override
	public List<Predmet> findNastavnikPredmeti(String email) {
		Nastavnik nastavnik = nastavnikRepo.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("Nastavnik nije pronađen s emailom: " + email));

		System.out.println("Predmeti: " + nastavnik.getPredmeti());
		return nastavnik.getPredmeti();
	}

	@Override
	public List<RasporedDTO> getRasporedZaNastavnika(String email) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

		
}
