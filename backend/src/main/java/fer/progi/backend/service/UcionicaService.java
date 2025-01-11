package fer.progi.backend.service;

import fer.progi.backend.domain.Ucionica;

import java.util.List;
import java.util.Optional;

public interface UcionicaService {

    List<Ucionica> listAll();

    Ucionica dodajUcionica(Ucionica ucionica);
    
	List<Ucionica> findAllUcionice();

	void deleteUcionica(String oznakaUc);

	Optional<Ucionica> findById(String oznakaUc);
}
