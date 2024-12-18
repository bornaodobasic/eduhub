package fer.progi.backend.service.impl;

import fer.progi.backend.dao.UcenikRepository;
import fer.progi.backend.domain.Razred;
import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.rest.UpisDTO;
import fer.progi.backend.service.RazredService;
import fer.progi.backend.service.UcenikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UcenikServiceJpa implements UcenikService {

    @Autowired
    private UcenikRepository ucenikRepo;

    @Autowired
    private RazredService razredService;

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
        ucenik.setDatumRodenja(upisDTO.getDatumRodenja());
        ucenik.setOib(upisDTO.getOib());
        ucenik.setEmail(email);

        Razred razred = razredService.getBestClass(upisDTO.getSmjer());
        ucenik.setRazred(razred);

        ucenikRepo.save(ucenik);

        return true;
    }


}
