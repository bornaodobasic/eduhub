package fer.progi.backend.service;

import java.util.List;

import fer.progi.backend.domain.Djelatnik;


public interface DjelatnikService {
	
	List<Djelatnik> listAll();
	
	Djelatnik dodajDjelatnik (Djelatnik djelatnik);

}
