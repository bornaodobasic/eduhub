package fer.progi.backend.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fer.progi.backend.domain.Admin;
import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.domain.Ucionica;
import fer.progi.backend.rest.AddDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fer.progi.backend.dao.*;
import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.domain.*;
import fer.progi.backend.service.SatnicarService;

@Service
public class SatnicarServiceJpa implements SatnicarService {

    @Autowired
    private SatnicarRepository satnicarRepo;
    
    @Autowired
    private UcionicaRepository ucionicaRepo;
    
    @Autowired SatRepository satRepo;

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
    public boolean createIfNeeded(AddDTO addDTO) {
        Optional<Satnicar> optionalSatnicar = satnicarRepo.findByEmail(addDTO.getEmail());
        if (optionalSatnicar.isEmpty()) {
            Satnicar satnicar = new Satnicar();
            satnicar.setEmail(addDTO.getEmail());
            satnicar.setImeSatnicar(addDTO.getIme());
            satnicar.setPrezimeSatnicar(addDTO.getPrezime());
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
        Satnicar satnicar = satnicarRepo.findByEmail(email).orElse(null);
        satnicarRepo.delete(satnicar);
    }

    @Override
    public Map<String, Double> pregledZauzecaUcionica() {
    	Map<String, Integer> ucioniceZauzece = new HashMap();
    	List<Ucionica> ucionice = ucionicaRepo.findAll();
    	
    	for(Ucionica c : ucionice) {
    		ucioniceZauzece.put(c.getOznakaUc(),0);
    	}
    	
    	List<Sat> sati = satRepo.findAll();
    	
    	for(Sat s : sati) {
    	 if(ucioniceZauzece.containsKey(s.getUcionica().getOznakaUc())) {
    		 ucioniceZauzece.put(s.getUcionica().getOznakaUc(), ucioniceZauzece.get(s.getUcionica().getOznakaUc()) + 1);
    	 }
    	}
    	
    	 Map<String, Double> ucioniceTjedanZauzece = new HashMap<>();
    	    for (Map.Entry<String, Integer> entry : ucioniceZauzece.entrySet()) {
    	        ucioniceTjedanZauzece.put(entry.getKey(), entry.getValue() / 35.0);
    	    }
    	
    	return ucioniceTjedanZauzece;
    	
    }
}