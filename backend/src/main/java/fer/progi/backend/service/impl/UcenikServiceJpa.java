package fer.progi.backend.service.impl;

import fer.progi.backend.dao.UcenikRepository;
import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.service.UcenikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class UcenikServiceJpa implements UcenikService {

    @Autowired
    private UcenikRepository ucenikRepo;

    @Override
    public List<Ucenik> listAll() {
        return ucenikRepo.findAll();
    }

    @Override
    public Ucenik dodajUcenika(Ucenik ucenik) {
        //Assert.notNull(ucenik, "Ocekuje se ucenik");
        return ucenikRepo.save(ucenik);
    }
}
