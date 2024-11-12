package fer.progi.backend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fer.progi.backend.dao.RazredRepository;
import fer.progi.backend.domain.Razred;
import fer.progi.backend.domain.Smjer;
import fer.progi.backend.service.RazredService;
import fer.progi.backend.service.RequestDeniedException;
import fer.progi.backend.service.SmjerService;

@Service
public class RazredServiceJpa implements RazredService{
	
	private static final String RAZRED_FORMAT = "[1-4][a-z]";
	
	@Autowired
	private RazredRepository razredRepo;
	
	@Autowired
    private SmjerService smjerService;

	@Override
	public List<Razred> listAll() {
		return razredRepo.findAll();
	}

//	@Override
//	public Razred dodajRazred(Razred razred) {
//		
//		Smjer smjer = smjerRepo.findById(razred.getSmjer().getSifSmjer()).orElseThrow(() -> new IllegalArgumentException("kriva šifra"));
//		
//		razred.setSmjer(smjer);
//		
//		return razredRepo.save(razred);
//	}
	
	public Razred addRazred(Razred razred) {
		String nazRazred = razred.getNazRazred();
		Assert.hasText(nazRazred, "Naziv razreda mora biti dan");
		Assert.isTrue(nazRazred.matches(RAZRED_FORMAT),
				"Naziv razreda mora imati broj 1-4 i malo slovo");
		if(razredRepo.countByNazRazred(nazRazred) > 0)
			throw new RequestDeniedException(
					"Razred " + nazRazred + " vec postoji!");
		
		Smjer smjer = smjerService.findBySifSmjer(razred.getSmjer().getSifSmjer());	
		razred.setSmjer(smjer);
		
		return razredRepo.save(razred);
	}

	public Razred findByNazRazred(String nazRazred) {
		return razredRepo.findByNazRazred(nazRazred)
				.orElseThrow(() -> new RuntimeException("Razred nije pronađen"));
	}
	
}
