package fer.progi.backend.service;

import fer.progi.backend.domain.Aktivnost;
import fer.progi.backend.domain.Ucenik;

import java.util.List;
import java.util.Set;

public interface UcenikService {
    List<Ucenik> listAll();

    //Ucenik dodajUcenika(Ucenik ucenik);
    
    //void dodajAktivnostUceniku(String oib, Integer sifAktivnost);
    
    //void dodajAktivnostUcenikuV2(Ucenik ucenik);

    Ucenik addUcenik(Ucenik ucenik);
    
    Set<Aktivnost> findByIds(Set<Integer> sifreAktivnosti);
}
