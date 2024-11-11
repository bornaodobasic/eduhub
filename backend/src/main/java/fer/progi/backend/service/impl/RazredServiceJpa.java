package fer.progi.backend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fer.progi.backend.dao.RazredRepository;
import fer.progi.backend.dao.SmjerRepository;
import fer.progi.backend.domain.Razred;
import fer.progi.backend.domain.Smjer;
import fer.progi.backend.service.RazredService;

@Service
public class RazredServiceJpa implements RazredService{
	
	@Autowired
	private RazredRepository razredRepo;
	
	@Autowired
    private SmjerRepository smjerRepo;

	@Override
	public List<Razred> listAll() {
		return razredRepo.findAll();
	}

	@Override
	public Razred dodajRazred(Razred razred) {
		
		Smjer smjer = smjerRepo.findById(razred.getSmjer().getSifSmjer()).orElseThrow(() -> new IllegalArgumentException("kriva šifra"));
		
		razred.setSmjer(smjer);
		
		return razredRepo.save(razred);
	}

	public Razred findByNazRazred(String nazRazred) {
		return razredRepo.findByNazRazred(nazRazred)
				.orElseThrow(() -> new RuntimeException("Razred nije pronađen"));
	}
	
	
}
