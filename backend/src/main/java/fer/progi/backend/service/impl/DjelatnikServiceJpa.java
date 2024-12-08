package fer.progi.backend.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fer.progi.backend.dao.DjelatnikRepository;
import fer.progi.backend.domain.Admin;
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

	/*	@Override
		public Optional<Djelatnik> pronadiDjelatnikaPoEmail(String email) {
			return Optional.ofNullable(djelatnikRepo.findByEmail(email));
		}
*/
		public boolean findByEmail(String email) {
			return djelatnikRepo.findByEmail(email).isPresent();
		}

		public Djelatnik getOrCreateDjelatnik(String email) {
			return djelatnikRepo.findByEmail(email)
					.orElseGet(() -> createNewDjelatnik(email));
		}

		private Djelatnik createNewDjelatnik(String email) {
			Djelatnik djelatnik = new Djelatnik();
			djelatnik.setEmail(email);
			return djelatnikRepo.save(djelatnik);
		}
		
}
