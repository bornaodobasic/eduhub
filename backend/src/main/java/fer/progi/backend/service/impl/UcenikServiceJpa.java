package fer.progi.backend.service.impl;

import fer.progi.backend.dao.AktivnostRepository;
import fer.progi.backend.dao.RazredRepository;
import fer.progi.backend.dao.UcenikRepository;
import fer.progi.backend.domain.Aktivnost;
import fer.progi.backend.domain.Razred;
import fer.progi.backend.domain.Ucenik;
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

	public Ucenik addUcenik(Ucenik ucenik) {

		String oib = ucenik.getOib();
		Assert.hasText(oib, "OIB mora biti dan");
		Assert.isTrue(oib.matches(OIB_FORMAT),
				"OIB mora imati 11 znakova"
		);
		if (ucenikRepo.countByOib(ucenik.getOib()) > 0)
			throw new RequestDeniedException(
					"Ucenik koji ima OIB " + ucenik.getOib() + " vec postoji!"
			);
		//Razred razred = razredRepo.findById(ucenik.getRazred().getNazRazred()).orElseThrow(() -> new RuntimeException("Razred nije pronađen"));
		Razred razred = razredService.findByNazRazred(ucenik.getRazred().getNazRazred());
		Set<Aktivnost> aktivnosti = findByIds(ucenik.getSifreAktivnost());

		ucenik.setRazred(razred);
		ucenik.setAktivnosti(aktivnosti);

		return ucenikRepo.save(ucenik);
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

}
