package fer.progi.backend.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import fer.progi.backend.rest.AddDTO;
import fer.progi.backend.rest.RasporedDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fer.progi.backend.dao.DjelatnikRepository;
import fer.progi.backend.domain.*;
import fer.progi.backend.service.DjelatnikService;
import fer.progi.backend.service.*;

@Service
public class DjelatnikServiceJpa implements DjelatnikService{
	
		
		@Autowired
		private DjelatnikRepository djelatnikRepo;
		
		@Autowired
		private NastavnikService nastavnikService;
		
		@Autowired
		private RazredPredmetNastavnikService rpnService;
		
		@Autowired
		private SatService satService;

		@Override
		public List<Djelatnik> listAll() {
			return djelatnikRepo.findAll();
		}

		@Override
		public Djelatnik dodajDjelatnik(Djelatnik djelatnik) {
			return djelatnikRepo.save(djelatnik);
		}

		public boolean findByEmail(String email) {
			return djelatnikRepo.findByEmail(email).isPresent();
		}

		@Override
		public boolean createIfNeeded(AddDTO addDTO) {
        	Optional<Djelatnik> optionalDjelatnik = djelatnikRepo.findByEmail(addDTO.getEmail());
			if (optionalDjelatnik.isEmpty()) {
				Djelatnik djelatnik = new Djelatnik();
				djelatnik.setEmail(addDTO.getEmail());
				djelatnik.setImeDjel(addDTO.getIme());
				djelatnik.setPrezimeDjel(addDTO.getPrezime());
				djelatnikRepo.save(djelatnik);
			}
			return true;
		}

	@Override
	public List<Djelatnik> findAllDjelatniks() {
		return djelatnikRepo.findAll();
	}

	@Override
	public void deleteDjelatnik(String email) {
		Djelatnik djelatnik = djelatnikRepo.findByEmail(email).orElse(null);
		djelatnikRepo.delete(djelatnik);
	}
	
	@Override
	public List<RasporedDTO> getRasporedZaNastavnika(String email) {
	    // 1. Pronađi nastavnika na osnovu emaila
	    Optional<Nastavnik> nastavnikOptional = nastavnikService.findByEmail(email);
	     Nastavnik nastavnik = nastavnikOptional.get();

	 
	    List<RazredPredmetNastavnik> rpnLista = rpnService.findByNastavnik(nastavnik);

	   
	    List<Sat> satiNastavnika = satService.listAll().stream()
	        .filter(sat -> rpnLista.contains(sat.getRpn()))
	        .toList();

	
	    List<RasporedDTO> raspored = new ArrayList<>();
	    for (Sat sat : satiNastavnika) {
	        RasporedDTO rasporedDTO = new RasporedDTO();
	        rasporedDTO.setDan(sat.getVrijemeSata().getDan());
	        rasporedDTO.setPredmet(sat.getRpn().getPredmet().getNazPredmet());
	        rasporedDTO.setUcionica(sat.getUcionica().getOznakaUc());
	        rasporedDTO.setNastavnik(nastavnik.getImeNastavnik() + " " + nastavnik.getPrezimeNastavnik());
	        rasporedDTO.setKrajSata(sat.getVrijemeSata().getKrajSata());
	        rasporedDTO.setPocetakSata(sat.getVrijemeSata().getPocetakSata());
	        raspored.add(rasporedDTO);
	    }


	    return raspored.stream()
	        .sorted(Comparator.comparing(RasporedDTO::getDan)
	                .thenComparing(RasporedDTO::getPocetakSata))
	        .toList();
	}

}
