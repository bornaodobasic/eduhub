package fer.progi.backend.service;

import java.util.List;
import java.util.Optional;

import fer.progi.backend.domain.Djelatnik;


public interface DjelatnikService {
	
	List<Djelatnik> listAll();
	
	Djelatnik dodajDjelatnik (Djelatnik djelatnik);

	Optional<Djelatnik> pronadiDjelatnikaPoEmail(String email);

}
