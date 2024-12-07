package fer.progi.backend.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fer.progi.backend.dao.RavnateljRepository;
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

		@Override
		public Optional<Ravnatelj> pronadiRavnateljaPoEmail(String email) {
			return Optional.ofNullable(ravnateljRepo.findByEmail(email));
		}
		
		
}