package fer.progi.backend.service;

import fer.progi.backend.domain.Ucionica;

import java.util.List;

public interface UcionicaService {

    List<Ucionica> listAll();

    Ucionica dodajUcionica(Ucionica ucionica);
}
