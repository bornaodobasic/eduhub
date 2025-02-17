package fer.progi.backend.service;

import fer.progi.backend.domain.Aktivnost;
import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.dto.RasporedDTO;
import fer.progi.backend.dto.UpisDTO;
import fer.progi.backend.domain.Predmet;

import java.util.List;
import java.util.Optional;

public interface UcenikService {

    boolean findByEmail(String email);
    
    boolean createNewUcenik(String email, UpisDTO upisDTO);

    List<Ucenik> findAllUceniks();
    
    void deleteUcenik(String email);

    List<Aktivnost> findUcenikAktivnosti(String email);

    List<Predmet> listAllPredmeti(String email);

	Optional<Ucenik> findByEmailUcenik(String email);
	
	boolean dodajAktivnostiPoNazivu(String email, List<String> oznAktivnosti);

    List<RasporedDTO> getRaspored(String email);

}
