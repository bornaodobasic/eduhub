package fer.progi.backend.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fer.progi.backend.dao.AktivnostRepository;
import fer.progi.backend.domain.Aktivnost;
import fer.progi.backend.service.AktivnostService;

@Service
public class AktivnostServiceJpa implements AktivnostService{
	
	@Autowired
	private AktivnostRepository aktivnostRepo;
	
	@Override
	public List<Aktivnost> listAll() {
		return aktivnostRepo.findAll();
	}

	@Override
	public Aktivnost dodajAktivnost(Aktivnost aktivnost) {
		return aktivnostRepo.save(aktivnost);
	}
	
	public Set<Aktivnost> findByIds(Set<Integer> sifreAktivnosti) {
		Set<Aktivnost> aktivnosti = new HashSet<>();
		for(Integer id : sifreAktivnosti) {
			Aktivnost aktivnost = aktivnostRepo.findById(id).orElseThrow(() -> new RuntimeException("Aktivnost nije pronađena"));
			aktivnosti.add(aktivnost);
		}
		
		return aktivnosti;
	}
}
