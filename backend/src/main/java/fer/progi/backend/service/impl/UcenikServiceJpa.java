package fer.progi.backend.service.impl;

import fer.progi.backend.dao.AktivnostRepository;
import fer.progi.backend.dao.UcenikRepository;
import fer.progi.backend.domain.Aktivnost;
import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.rest.UpisDTO;
import fer.progi.backend.service.AktivnostService;
import fer.progi.backend.service.RazredService;
import fer.progi.backend.service.UcenikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UcenikServiceJpa implements UcenikService {

    @Autowired
    private UcenikRepository ucenikRepo;

    @Override
    public boolean findByEmail(String email) {
        return ucenikRepo.findByEmail(email).isPresent();
    }

    @Override
    public boolean createNewUcenik(String email, UpisDTO upisDTO) {
        Ucenik ucenik = new Ucenik();

        ucenik.setImeUcenik(upisDTO.getImeUcenik());
        ucenik.setPrezimeUcenik(upisDTO.getPrezimeUcenik());
        ucenik.setSpol(upisDTO.getSpol());
        ucenik.setEmail(email);

        ucenikRepo.save(ucenik);

        return true;
    }


}
