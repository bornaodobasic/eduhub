package fer.progi.backend.service.impl;

import fer.progi.backend.dao.AktivnostRepository;
import fer.progi.backend.dao.UcenikRepository;
import fer.progi.backend.domain.Aktivnost;
import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.service.AktivnostService;
import fer.progi.backend.service.RazredService;
import fer.progi.backend.service.UcenikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UcenikServiceJpa implements UcenikService {

    @Autowired
    private UcenikRepository ucenikRepo;
    
	@Autowired
	private RazredService razredService;
    
    @Autowired
	private AktivnostRepository aktivnostRepo;
    
    @Autowired
   	private AktivnostService aktivnostService;

    @Override
    public List<Ucenik> listAll() {
        return ucenikRepo.findAll();
    }
	
	public Set<Aktivnost> findByIds(Set<Integer> sifreAktivnosti) {
		Set<Aktivnost> aktivnosti = new HashSet<>();
		for(Integer id : sifreAktivnosti) {
			//Aktivnost aktivnost = aktivnostRepo.findById(id).orElseThrow(() -> new RuntimeException("Aktivnost nije pronađena"));
			Aktivnost aktivnost = aktivnostService.findBySifAktivnost(id);

			aktivnosti.add(aktivnost);
		}
		
		return aktivnosti;
	}

	@Override
	public boolean addActivity(Integer sifra) {
		return false;
	}
	/*
	public boolean addActivity(Integer sifra) {
		Optional<Aktivnost> aktivnostMaybe = aktivnostRepo.findById(sifra);

		if (aktivnostMaybe.isPresent()) {
			Aktivnost aktivnost = aktivnostMaybe.get();
			Set<Aktivnost> aktivnostSet = uce
		}
	}
	*/

	public boolean existsByEmail(String email) {
		return ucenikRepo.findByEmail(email).isPresent();
	}

	public Optional<Ucenik> findByEmail(String email) {
		return ucenikRepo.findByEmail(email);
	}

	
	public boolean createNewUcenik(String email, String imeUcenik, String prezimeUcenik, String spol) {

	        Ucenik ucenik = new Ucenik();
	        ucenik.setImeUcenik(imeUcenik);
	        ucenik.setPrezimeUcenik(prezimeUcenik);
	        ucenik.setSpol(spol);
			ucenik.setEmail(email);
	        ucenikRepo.save(ucenik);

	        return true;
	}


}
