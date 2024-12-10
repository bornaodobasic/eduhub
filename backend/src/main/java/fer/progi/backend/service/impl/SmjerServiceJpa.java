package fer.progi.backend.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fer.progi.backend.dao.SmjerRepository;
import fer.progi.backend.domain.Smjer;
import fer.progi.backend.service.SmjerService;

@Service
public class SmjerServiceJpa implements SmjerService {

    @Autowired
    private SmjerRepository smjerRepo;

    @Override
    public List<Smjer> listAll() {
        return smjerRepo.findAll();
    }

    @Override
    public Smjer dodajSmjer(String naziv) {
        Smjer smjer = new Smjer();
        smjer.setNazivSmjer(naziv);
        return smjerRepo.save(smjer);
    }

	@Override
	public Smjer findByNazSmjer(String naziv) {
        Optional<Smjer> smjer = smjerRepo.findByNazSmjer(naziv);
        if (smjer.isEmpty()) { return dodajSmjer(naziv); }
        else return smjer.get();
	}
}
