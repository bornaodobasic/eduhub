package fer.progi.backend.service;

import fer.progi.backend.domain.Predmet;

import java.util.List;
import java.util.Set;

public interface NastavnikPredmetService {

	boolean dodajPredmetNastavnik(String email, List<String> predmeti);

	boolean ukloniPredmetNastavnik(String email, List<String> predmeti);

	Set<Predmet> findNotNastavnikPredmeti(String email);


}
