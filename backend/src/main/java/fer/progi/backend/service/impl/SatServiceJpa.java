package fer.progi.backend.service.impl;

import fer.progi.backend.dao.SatRepository;
import fer.progi.backend.service.RazredPredmetNastavnikService;
import fer.progi.backend.service.VrijemeSataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SatServiceJpa implements SatService {

    @Autowired
    private RazredPredmetNastavnikService rpnService;

    @Autowired
    private VrijemeSataService vrijemeSataService;

    @Autowired
    private SatRepository satRepo;

    @Override
    public void generirajRaspored() {



    }
}
