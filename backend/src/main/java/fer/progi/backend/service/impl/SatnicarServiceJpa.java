package fer.progi.backend.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fer.progi.backend.dao.SatnicarRepository;
import fer.progi.backend.domain.Satnicar;
import fer.progi.backend.service.SatnicarService;

@Service
public class SatnicarServiceJpa implements SatnicarService{	
		
		@Autowired
		private SatnicarRepository satnicarRepo;

		@Override
		public List<Satnicar> listAll() {
			return satnicarRepo.findAll();
		}

		@Override
		public Satnicar dodajSatnicara (Satnicar satnicar) {
			return satnicarRepo.save(satnicar);
		}

		@Override
		public Optional<Satnicar> pronadiSatnicaraPoEmail(String email) {
			return Optional.ofNullable(satnicarRepo.findByEmail(email));
		}
		
		
}