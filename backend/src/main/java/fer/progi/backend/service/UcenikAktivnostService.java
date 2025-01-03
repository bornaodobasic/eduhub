package fer.progi.backend.service;

import fer.progi.backend.domain.Aktivnost;

import java.util.List;
import java.util.Set;

public interface UcenikAktivnostService {
	
	boolean dodajUcenikAktivnost(String email, List<String> naziviAktivnosti);

	boolean ukloniUcenikAktivnost(String email, List<String> naziviAktivnosti);

	Set<Aktivnost> findNotUcenikAktivnosti(String email);

}
