package fer.progi.backend.service.impl;

import java.util.List;
import fer.progi.backend.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fer.progi.backend.domain.Admin;
import fer.progi.backend.service.AdminService;


@Service
public class AdminServiceJpa implements AdminService{

		@Autowired
		private AdminRepository adminRepo;

		@Autowired
		private NastavnikRepository nastavnikRepo;

		@Autowired
		private DjelatnikRepository djelatnikRepo;

		@Autowired
		private UcenikRepository ucenikRepo;

		@Autowired
		private RavnateljRepository ravnateljRepo;

		@Autowired
		private SatnicarRepository satnicarRepo;


		@Override
		public List<Admin> listAll() {
			return adminRepo.findAll();
		}

		@Override
		public Admin dodajAdmin(Admin admin) {
			return adminRepo.save(admin);
		}

		public Admin getOrCreateAdmin(String email) {
			return adminRepo.findByEmail(email)
					.orElseGet(() -> createNewAdmin(email));
		}

		private Admin createNewAdmin(String email) {
			Admin admin = new Admin();
			admin.setEmail(email);
			return adminRepo.save(admin);
		}

}
