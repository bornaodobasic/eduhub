package fer.progi.backend.service;

import fer.progi.backend.domain.Aktivnost;
import fer.progi.backend.domain.Ucenik;

import java.util.List;
import java.util.Set;

public interface UcenikService {
    List<Ucenik> listAll();
    
    Set<Aktivnost> findByIds(Set<Integer> sifreAktivnosti);

    boolean addActivity(Integer sifra);

}
