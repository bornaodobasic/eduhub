package fer.progi.backend.service.impl;

import fer.progi.backend.domain.Predmet;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fer.progi.backend.dao.PredmetRepository;
import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.domain.Smjer;
import fer.progi.backend.service.NastavnikService;
import fer.progi.backend.service.PredmetService;
import fer.progi.backend.service.RequestDeniedException;
import fer.progi.backend.service.SmjerService;

@Service
public class PredmetServiceJpa implements PredmetService{
	
	@Autowired
	private PredmetRepository predmetRepo;
	
	
	@Autowired
    private SmjerService smjerService;
	
	@Autowired
	private NastavnikService nastavnikService;

	@Override
	public List<Predmet> listAll() {
		return predmetRepo.findAll();
	}



	
	public void dodajNastavnikaUPredmet(Integer sifPredmet, Integer sifNastavnik) {
		Predmet predmet = predmetRepo.findById(sifPredmet).orElseThrow(() -> new IllegalArgumentException("krivi predmet"));
		//Nastavnik nastavnik = nastavnikRepo.findById(sifNastavnik).orElseThrow(() -> new IllegalArgumentException("krivi nastavnik"));
		Nastavnik nastavnik = nastavnikService.findBySifNastavnik(sifNastavnik);
		predmet.getNastavnici().add(nastavnik);
		predmetRepo.save(predmet);
		
	}
	
}
