package fer.progi.backend.service.impl;

import fer.progi.backend.domain.Predmet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fer.progi.backend.dao.NastavnikRepository;
import fer.progi.backend.dao.PredmetRepository;
import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.domain.Smjer;
import fer.progi.backend.service.PredmetService;
import fer.progi.backend.service.RequestDeniedException;
import fer.progi.backend.service.SmjerService;

@Service
public class PredmetServiceJpa implements PredmetService{
	
	@Autowired
	private PredmetRepository predmetRepo;
	
	@Autowired
	private NastavnikRepository nastavnikRepo;
	
	@Autowired
    private SmjerService smjerService;

	@Override
	public List<Predmet> listAll() {
		return predmetRepo.findAll();
	}

//	@Override
//	public Predmet dodajPredmet(Predmet predmet) {
//		
//		Smjer smjer = smjerRepo.findById(predmet.getSmjer().getSifSmjer()).orElseThrow(() -> new IllegalArgumentException("kriva šifra"));
//		
//		predmet.setSmjer(smjer);
//		return predmetRepo.save(predmet);
//	}
	
	public void dodajNastavnikaUPredmet(Integer sifPredmet, Integer sifNastavnik) {
		Predmet predmet = predmetRepo.findById(sifPredmet).orElseThrow(() -> new IllegalArgumentException("krivi predmet"));
		Nastavnik nastavnik = nastavnikRepo.findById(sifNastavnik).orElseThrow(() -> new IllegalArgumentException("krivi nastavnik"));
		
		predmet.getNastavnici().add(nastavnik);
		predmetRepo.save(predmet);
		
	}

	@Override
	public Predmet addPredmet(Predmet predmet) {
		
		if(predmetRepo.countBySifPredmet(predmet.getSifPredmet()) >0)
			throw new RequestDeniedException(
					"Predmet sa šifrom " + predmet.getSifPredmet() + " vec postoji!"
			);
		Smjer smjer = smjerService.findBySifSmjer(predmet.getSmjer().getSifSmjer());
		
		predmet.setSmjer(smjer);
		return predmetRepo.save(predmet);
	}

}
