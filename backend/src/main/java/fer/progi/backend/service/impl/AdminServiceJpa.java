package fer.progi.backend.service.impl;

import java.util.List;
import java.util.Optional;

import fer.progi.backend.dao.NastavnikRepository;
import fer.progi.backend.dao.TempNastavnikRepository;
import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.domain.TempNastavnik;
import fer.progi.backend.rest.RegisterNastavnikDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fer.progi.backend.dao.AdminRepository;
import fer.progi.backend.domain.Admin;
import fer.progi.backend.service.AdminService;


@Service
public class AdminServiceJpa implements AdminService{
	
		
		@Autowired
		private AdminRepository adminRepo;

		@Autowired
		private TempNastavnikRepository tempNastavnikRepo;

		@Autowired
		private NastavnikRepository nastavnikRepo;

		@Autowired
		private PasswordEncoder passwordEncoder;

		@Override
		public List<Admin> listAll() {
			return adminRepo.findAll();
		}

		@Override
		public Admin dodajAdmin(Admin admin) {
			return adminRepo.save(admin);
		}

		public List<TempNastavnik> dohvatiSveZahtjeveNastavnika() {
			return tempNastavnikRepo.findAll();
		}
		@Override
		public boolean addNastavnikToTempDB(RegisterNastavnikDTO registerNastavnikDTO) {
			TempNastavnik tempNastavnik = new TempNastavnik();
			tempNastavnik.setImeNastavnik(registerNastavnikDTO.getImeNastavnik());
			tempNastavnik.setPrezimeNastavnik(registerNastavnikDTO.getPrezimeNastavnik());
			tempNastavnik.setEmail(registerNastavnikDTO.getEmail());
			tempNastavnik.setLozinka(passwordEncoder.encode(registerNastavnikDTO.getLozinka()));

			TempNastavnik saved = tempNastavnikRepo.save(tempNastavnik);

			return saved != null;
		}

		public boolean odobriNastavnika(TempNastavnik tempNastavnik) {
			Nastavnik nastavnik = new Nastavnik();
			nastavnik.setImeNastavnik(tempNastavnik.getImeNastavnik());
			nastavnik.setPrezimeNastavnik(tempNastavnik.getPrezimeNastavnik());
			nastavnik.setEmail(tempNastavnik.getEmail());
			nastavnik.setLozinka(tempNastavnik.getLozinka());

			nastavnikRepo.save(nastavnik);
			tempNastavnikRepo.delete(tempNastavnik);

			return true;
		}

		public boolean odbaciNastavnika(String email) {
			Optional<TempNastavnik> tempNastavnik = tempNastavnikRepo.findById(email);
			if (tempNastavnik.isPresent()) {
				tempNastavnikRepo.delete(tempNastavnik.get());
				return true;
			}
			return false;
		}

		public Optional<TempNastavnik> dohvatiZahtjevNastavnikaPoId(String email) {
			return tempNastavnikRepo.findById(email);
		}


		
		
}
