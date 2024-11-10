package fer.progi.backend.service;

import fer.progi.backend.domain.Ucenik;

import java.util.List;

public interface UcenikService {
    List<Ucenik> listAll();

    Ucenik dodajUcenika(Ucenik ucenik);
}
