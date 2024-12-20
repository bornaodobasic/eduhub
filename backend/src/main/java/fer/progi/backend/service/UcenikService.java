package fer.progi.backend.service;

import fer.progi.backend.domain.Aktivnost;
import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.rest.UpisDTO;

import java.util.List;
import java.util.Set;

public interface UcenikService {

    boolean findByEmail(String email);
    
    boolean createNewUcenik(String email, UpisDTO upisDTO);

    List<Ucenik> findAllUceniks();
    
    void deleteUcenik(String email);

    Set<Aktivnost> findUcenikAktivnosti(String email);

}
