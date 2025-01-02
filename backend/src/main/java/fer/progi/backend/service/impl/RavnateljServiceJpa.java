package fer.progi.backend.service.impl;

import java.util.List;
import java.util.Optional;

import fer.progi.backend.domain.Admin;
import fer.progi.backend.domain.Ucenik;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fer.progi.backend.dao.RavnateljRepository;
import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.domain.Ravnatelj;
import fer.progi.backend.service.RavnateljService;


@Service
public class RavnateljServiceJpa implements RavnateljService{
	
		
		@Autowired
		private RavnateljRepository ravnateljRepo;

		@Override
		public List<Ravnatelj> listAll() {
			return ravnateljRepo.findAll();
		}

		@Override
		public Ravnatelj dodajRavnatelj(Ravnatelj ravnatelj) {
			return ravnateljRepo.save(ravnatelj);
		}

		public boolean findByEmail(String email) {
			return ravnateljRepo.findByEmail(email).isPresent();
		}

		@Override
		public boolean createIfNeeded(String email) {
        	Optional<Ravnatelj> optionalRavnatelj = ravnateljRepo.findByEmail(email);
			if (optionalRavnatelj.isEmpty()) {
				Ravnatelj ravnatelj = new Ravnatelj();
				ravnatelj.setEmail(email);
				ravnateljRepo.save(ravnatelj);
			}
			return true;
		}

	@Override
	public List<Ravnatelj> findAllRavnateljs() {
		return ravnateljRepo.findAll();
	}

	@Override
	public void deleteRavnatelj(String email) {
		Ravnatelj ravnatelj = ravnateljRepo.findByEmail(email).orElse(null);
		ravnateljRepo.delete(ravnatelj);
	}
		
}