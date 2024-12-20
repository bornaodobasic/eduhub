package fer.progi.backend.service.impl;

import fer.progi.backend.domain.Predmet;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fer.progi.backend.dao.PredmetRepository;
import fer.progi.backend.service.PredmetService;


@Service
public class PredmetServiceJpa implements PredmetService{
	
	@Autowired
	private PredmetRepository predmetRepo;


	@Override
	public List<Predmet> listAll() {
		return predmetRepo.findAll();
	}

	@Override
	public Predmet findPredmetByNaz(String nazPredmet) {
		return predmetRepo.findByNazPredmet(nazPredmet);
	}
	
}
