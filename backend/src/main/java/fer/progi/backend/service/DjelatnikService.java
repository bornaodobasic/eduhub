package fer.progi.backend.service;

import java.util.List;
import java.util.Optional;

import fer.progi.backend.domain.Admin;
import fer.progi.backend.domain.Djelatnik;


public interface DjelatnikService {
	
	List<Djelatnik> listAll();
	
	Djelatnik dodajDjelatnik (Djelatnik djelatnik);

	boolean createIfNeeded(String email);

	List<Djelatnik> findAllDjelatniks();
	void deleteDjelatnik(String email);

}
