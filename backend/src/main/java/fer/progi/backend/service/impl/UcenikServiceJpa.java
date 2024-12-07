package fer.progi.backend.service.impl;

import fer.progi.backend.dao.AktivnostRepository;
import fer.progi.backend.dao.UcenikRepository;
import fer.progi.backend.domain.Aktivnost;
import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.domain.Razred;
import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.rest.RegisterUcenikDTO;
import fer.progi.backend.service.AktivnostService;
import fer.progi.backend.service.RazredService;
import fer.progi.backend.service.RequestDeniedException;
import fer.progi.backend.service.UcenikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UcenikServiceJpa implements UcenikService {

	private static final String OIB_FORMAT = "[0-9]{11}";

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

	/*@Override
	public Optional<Ucenik> pronadiUcenikaPoEmail(String email) {
		return Optional.ofNullable(ucenikRepo.findByEmail(email));
	}*/

//	public boolean existsByEmail(String email) {
//		return ucenikRepo.findByEmail(email).isPresent();
//	}
	
	public Ucenik getOrCreateUcenik(String email) {
		return ucenikRepo.findByEmail(email)
				.orElseGet(() -> createNewUcenik(email));
}
	
	 public boolean isFirstClass(String email) {
		 return ucenikRepo.findByEmail(email)
		            .map(ucenik -> ucenik.getImeUcenik() == null)
		            .orElse(true);
	 }
	


//	public Optional<Ucenik> getUcenik(String email) {
//		return ucenikRepo.findByEmail(email);
//	}

	private Ucenik createNewUcenik(String email) {
		Ucenik ucenik = new Ucenik();
		ucenik.setEmail(email);
		return ucenikRepo.save(ucenik);
	}
	
	public boolean createNewUcenikFirst(String email, String imeUcenik, String prezimeUcenik, String spol) {
	    Optional<Ucenik> optionalUcenik = ucenikRepo.findByEmail(email);
	    if (optionalUcenik.isPresent()) {
	        Ucenik ucenik = optionalUcenik.get();
	        ucenik.setImeUcenik(imeUcenik);
	        ucenik.setPrezimeUcenik(prezimeUcenik);
	        ucenik.setSpol(spol);
	        ucenikRepo.save(ucenik);
	        return true;
	    }
	    return false;
	}


}
