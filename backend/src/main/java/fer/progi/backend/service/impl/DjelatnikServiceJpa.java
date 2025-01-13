package fer.progi.backend.service.impl;

import java.util.List;
import java.util.Optional;

import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.rest.AddDTO;
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

		public boolean findByEmail(String email) {
			return djelatnikRepo.findByEmail(email).isPresent();
		}

		@Override
		public boolean createIfNeeded(AddDTO addDTO) {
        	Optional<Djelatnik> optionalDjelatnik = djelatnikRepo.findByEmail(addDTO.getEmail());
			if (optionalDjelatnik.isEmpty()) {
				Djelatnik djelatnik = new Djelatnik();
				djelatnik.setEmail(addDTO.getEmail());
				djelatnik.setImeDjel(addDTO.getIme());
				djelatnik.setPrezimeDjel(addDTO.getPrezime());
				djelatnikRepo.save(djelatnik);
			}
			return true;
		}

	@Override
	public List<Djelatnik> findAllDjelatniks() {
		return djelatnikRepo.findAll();
	}

	@Override
	public void deleteDjelatnik(String email) {
		Djelatnik djelatnik = djelatnikRepo.findByEmail(email).orElse(null);
		djelatnikRepo.delete(djelatnik);
	}
}
