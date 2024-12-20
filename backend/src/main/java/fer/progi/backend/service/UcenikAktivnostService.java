package fer.progi.backend.service;

import java.util.List;

public interface UcenikAktivnostService {
	
	boolean dodajUcenikAktivnost(String email, List<String> naziviAktivnosti);

	boolean ukloniUcenikAktivnost(String email, List<String> naziviAktivnosti);

}
