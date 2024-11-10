package fer.progi.backend.service.impl;

import java.util.List;

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
		
		
}
