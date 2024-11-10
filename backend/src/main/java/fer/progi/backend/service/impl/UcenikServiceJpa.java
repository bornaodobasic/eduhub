package fer.progi.backend.service.impl;

import fer.progi.backend.dao.AktivnostRepository;
import fer.progi.backend.dao.RazredRepository;
import fer.progi.backend.dao.UcenikRepository;
import fer.progi.backend.domain.Aktivnost;
import fer.progi.backend.domain.Razred;
import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.service.UcenikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UcenikServiceJpa implements UcenikService {

    @Autowired
    private UcenikRepository ucenikRepo;
    
    @Autowired
	private RazredRepository razredRepo;
    
    @Autowired
	private AktivnostRepository aktivnostRepo;

    @Override
    public List<Ucenik> listAll() {
        return ucenikRepo.findAll();
    }

    @Override
    public Ucenik dodajUcenika(Ucenik ucenik) {
        
    	Razred razred = razredRepo.findById(ucenik.getRazred().getNazRazred()).orElseThrow(() -> new IllegalArgumentException("krivi razred"));
    	ucenik.setRazred(razred);
    	
        return ucenikRepo.save(ucenik);
    }

	@Override
	public void dodajAktivnostUceniku(String oib, Integer sifAktivnost) {
		Ucenik ucenik = ucenikRepo.findById(oib).orElseThrow(() -> new IllegalArgumentException("krivi ucenik"));
		Aktivnost aktivnost = aktivnostRepo.findById(sifAktivnost).orElseThrow(() -> new IllegalArgumentException("kriva aktivnost"));
		
		ucenik.getAktivnosti().add(aktivnost);
		ucenikRepo.save(ucenik);
		
	}
	
	public void dodajAktivnostUcenikuV2(Ucenik ucenik) {
		Set<Aktivnost> aktivnosti = findByIds(ucenik.getSifreAktivnost());
		ucenik.setAktivnosti(aktivnosti);
		ucenikRepo.save(ucenik);
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
