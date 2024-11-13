package fer.progi.backend.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fer.progi.backend.dao.DjelatnikRepository;
import fer.progi.backend.domain.Djelatnik;
import fer.progi.backend.service.DjelatnikService;

@Service
public class DjelatnikServiceJpa implements DjelatnikService{
	
		
		@Autowired
		private DjelatnikRepository djelatnikRepo;

		@Override
		public List<Djelatnik> listAll() {
			return djelatnikRepo.findAll();
		}

		@Override
		public Djelatnik dodajDjelatnik(Djelatnik djelatnik) {
			return djelatnikRepo.save(djelatnik);
		}

		@Override
		public Optional<Djelatnik> pronadiDjelatnikaPoEmail(String email) {
			return Optional.ofNullable(djelatnikRepo.findByEmail(email));
		}
		
		
}
