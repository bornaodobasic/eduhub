package fer.progi.backend.service.impl;

import fer.progi.backend.dao.UcionicaRepository;
import fer.progi.backend.domain.Ucionica;
import fer.progi.backend.service.UcionicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UcionicaServiceJpa implements UcionicaService {

    @Autowired
    private UcionicaRepository ucionicaRepo;

    @Override
    public List<Ucionica> listAll() {
        return ucionicaRepo.findAll();
    }

    @Override
    public Ucionica dodajUcionica(Ucionica ucionica) {
        return ucionicaRepo.save(ucionica);
    }
    
	@Override
	public List<Ucionica> findAllUcionice() {
		return ucionicaRepo.findAll();
	}

	@Override
	public void deleteUcionica(String oznakaUc) {
		ucionicaRepo.deleteById(oznakaUc);
		
	}

	@Override
	public Optional<Ucionica> findById(String oznakaUc) {
			return ucionicaRepo.findById(oznakaUc);
		}
}
