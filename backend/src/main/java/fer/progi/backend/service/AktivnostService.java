package fer.progi.backend.service;

import java.util.List;
import java.util.Set;

import fer.progi.backend.domain.Aktivnost;

public interface AktivnostService {
	
	List<Aktivnost> listAll();

	Aktivnost dodajAktivnost(Aktivnost aktivnost);
	
	Set<Aktivnost> findByIds(Set<Integer> sifreAktivnosti);

	Aktivnost findBySifAktivnost(Integer id);

	
}
