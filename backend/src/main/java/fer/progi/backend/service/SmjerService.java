package fer.progi.backend.service;

import java.util.List;
import java.util.Optional;

import fer.progi.backend.domain.Smjer;

public interface SmjerService {
	
	List<Smjer> listAll();
	Smjer dodajSmjer(String naziv);

	Smjer findByNazSmjer(String naziv);

}
