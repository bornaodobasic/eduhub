package fer.progi.backend.service.impl;

import java.util.List;

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
    public Smjer dodajSmjer(Smjer smjer) {
        return smjerRepo.save(smjer);
    }

	@Override
	public Smjer findBySifSmjer(Integer sifSmjer) {
		return smjerRepo.findBySifSmjer(sifSmjer).orElseThrow(() -> new RuntimeException("Smjer nije pronađen"));
	}
}
