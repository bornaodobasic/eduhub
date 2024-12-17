package fer.progi.backend.service.impl;

import java.util.List;
import java.util.Optional;

import fer.progi.backend.domain.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fer.progi.backend.dao.SatnicarRepository;
import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.domain.Satnicar;
import fer.progi.backend.service.SatnicarService;

@Service
public class SatnicarServiceJpa implements SatnicarService {

    @Autowired
    private SatnicarRepository satnicarRepo;

    @Override
    public List<Satnicar> listAll() {
        return satnicarRepo.findAll();
    }

    @Override
    public Satnicar dodajSatnicara(Satnicar satnicar) {
        return satnicarRepo.save(satnicar);
    }

    public boolean findByEmail(String email) {
        return satnicarRepo.findByEmail(email).isPresent();
    }

    @Override
    public boolean createIfNeeded(String email) {
        Optional<Satnicar> optionalSatnicar = satnicarRepo.findByEmail(email);
        if (optionalSatnicar.isEmpty()) {
            Satnicar satnicar = new Satnicar();
            satnicar.setEmail(email);
            satnicarRepo.save(satnicar);
        }
        return true;
    }


    @Override
    public List<Satnicar> findAllSatnicars() {
        return satnicarRepo.findAll();
    }

    @Override
    public void deleteSatnicar(String email) {
        satnicarRepo.deleteByEmail(email);
    }
}