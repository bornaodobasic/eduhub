package fer.progi.backend.service;

import java.util.List;

public interface NastavnikPredmetService {

	boolean dodajPredmetNastavnik(String email, List<String> predmeti);

	boolean ukloniPredmetNastavnik(String email, List<String> predmeti);


}
