package fer.progi.backend.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fer.progi.backend.dao.RazredRepository;
import fer.progi.backend.domain.Razred;
import fer.progi.backend.service.RazredService;
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


	@Override
	public void dodajRazred(String nazRazred, String nazivSmjer) {
		Razred razred = new Razred();
		razred.setNazRazred(nazRazred);
		razred.setSmjer(smjerService.findByNazSmjer(nazivSmjer));
		razredRepo.save(razred);
	}

	@Override
	public Razred getBestClass(String smjer) {
		List<Razred> tempRazredi = razredRepo.findAllBySmjer_NazivSmjer(smjer);
		for (Razred razred : tempRazredi) {
			System.out.println(razred.getNazRazred());
		}
		tempRazredi.removeIf(razred -> !razred.getNazRazred().startsWith("1"));


		Razred bestClass = tempRazredi.getFirst();
		for(Razred razred : tempRazredi){
			int ucenikCount = razred.getUcenici().size();
			if (ucenikCount < bestClass.getUcenici().size()) {
				bestClass = razred;
			}
		}
		return bestClass;
	}

	public boolean findByNazRazred(String nazRazred) {
		Optional<Razred> optionalRazred = razredRepo.findByNazRazred(nazRazred);
        return optionalRazred.isPresent();
	}

	@Override
	public Razred findByNazivRazred(String nazRazred) {
		Optional<Razred> optionalRazred = razredRepo.findByNazRazred(nazRazred);
        return optionalRazred.orElse(null);
	}

	@Override
	public Razred findRazred(String nazRazred) {
		Optional<Razred> optionalRazred = razredRepo.findByNazRazred(nazRazred);
		return optionalRazred.orElse(null);
	}


}
