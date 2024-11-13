package fer.progi.backend.service;

import java.util.List;

import fer.progi.backend.domain.Smjer;

public interface SmjerService {
	
	List<Smjer> listAll();

	Smjer dodajSmjer(Smjer smjer);
	
	Smjer findBySifSmjer(Integer sifSmjer);
}
