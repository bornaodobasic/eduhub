package fer.progi.backend.service;

import java.util.List;
import java.util.Optional;

import fer.progi.backend.domain.Admin;
import fer.progi.backend.domain.Djelatnik;
import fer.progi.backend.rest.AddDTO;
import fer.progi.backend.rest.RasporedDTO;


public interface DjelatnikService {
	
	List<Djelatnik> listAll();
	
	Djelatnik dodajDjelatnik (Djelatnik djelatnik);

	boolean createIfNeeded(AddDTO addDTO);

	List<Djelatnik> findAllDjelatniks();
	void deleteDjelatnik(String email);

	List<RasporedDTO> getRasporedZaNastavnika(String email);

}
