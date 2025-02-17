package fer.progi.backend.service;

import java.util.List;

import fer.progi.backend.domain.*;
import fer.progi.backend.dto.AddDTO;
import fer.progi.backend.dto.AdminAddUcenikDTO;


public interface AdminService {
	
	boolean findByEmail(String email);
	
	Admin addAdmin (AddDTO addDTO);
	Djelatnik addDjelatnik (AddDTO addDTO);
	Nastavnik addNastavnik (AddDTO addDTO);
	Ravnatelj addRavnatelj (AddDTO addDTO);
	Satnicar addSatnicar (AddDTO addDTO);
	Ucenik addUcenik (AdminAddUcenikDTO adminAddUcenikDTO);
	boolean createIfNeeded(AddDTO addDTO);
	List<Admin> findAllAdmins();
	void deleteAdmin(String email);
}
