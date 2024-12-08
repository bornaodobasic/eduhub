package fer.progi.backend.service;

import java.util.List;

import fer.progi.backend.domain.Admin;


public interface AdminService {
	
	List<Admin> listAll();
	
	Admin dodajAdmin (Admin admin);

}
