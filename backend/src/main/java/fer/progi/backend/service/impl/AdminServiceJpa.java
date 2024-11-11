package fer.progi.backend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fer.progi.backend.dao.AdminRepository;
import fer.progi.backend.domain.Admin;
import fer.progi.backend.service.AdminService;



@Service
public class AdminServiceJpa implements AdminService{
	
		
		@Autowired
		private AdminRepository adminRepo;

		@Override
		public List<Admin> listAll() {
			return adminRepo.findAll();
		}

		@Override
		public Admin dodajAdmin(Admin admin) {
			return adminRepo.save(admin);
		}

		@Override
		public boolean odobriZahtjev() {
			// TODO Auto-generated method stub
			return false;
		}
		
		
}
