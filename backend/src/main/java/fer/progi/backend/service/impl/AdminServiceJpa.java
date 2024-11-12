package fer.progi.backend.service.impl;

import java.util.List;
import java.util.Optional;

import fer.progi.backend.dao.NastavnikRepository;
import fer.progi.backend.dao.RavnateljRepository;
import fer.progi.backend.dao.SatnicarRepository;
import fer.progi.backend.dao.TempNastavnikRepository;
import fer.progi.backend.dao.TempRavnateljRepository;
import fer.progi.backend.dao.TempSatnicarRepository;
import fer.progi.backend.dao.TempUcenikRepository;
import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.domain.Ravnatelj;
import fer.progi.backend.domain.Satnicar;
import fer.progi.backend.domain.TempDjelatnik;
import fer.progi.backend.domain.TempNastavnik;
import fer.progi.backend.domain.TempRavnatelj;
import fer.progi.backend.domain.TempSatnicar;
import fer.progi.backend.domain.TempUcenik;
import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.rest.RegisterDjelatnikDTO;
import fer.progi.backend.rest.RegisterNastavnikDTO;
import fer.progi.backend.rest.RegisterRavnateljDTO;
import fer.progi.backend.rest.RegisterSatnicarDTO;
import fer.progi.backend.rest.RegisterUcenikDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fer.progi.backend.dao.AdminRepository;
import fer.progi.backend.domain.Admin;
import fer.progi.backend.domain.Djelatnik;
import fer.progi.backend.service.AdminService;


@Service
public class AdminServiceJpa implements AdminService{
		
		@Autowired
		private AdminRepository adminRepo;
		
		@Autowired
		private TempUcenikRepository tempUcenikRepo;

		@Autowired
		private TempNastavnikRepository tempNastavnikRepo;

		@Autowired
		private NastavnikRepository nastavnikRepo;
		
		@Autowired
		private fer.progi.backend.dao.DjelatnikRepository djelatnikRepo;
		
		@Autowired
		private fer.progi.backend.dao.UcenikRepository ucenikRepo;
		
		@Autowired
		private fer.progi.backend.dao.TempDjelatnikRepository tempDjelatnikRepo;
		
		@Autowired
		private TempRavnateljRepository tempRavnateljRepo;
			
		@Autowired
		private TempSatnicarRepository tempSatnicarRepo;
		
		@Autowired
		private RavnateljRepository ravnateljRepo;
		
		@Autowired
		private SatnicarRepository satnicarRepo;

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
		
		//Ucenik --------------------------------------------------------------------------------------------------------------------
		
		
		public List<TempUcenik> dohvatiSveZahtjeveUcenika() {
			return tempUcenikRepo.findAll();
		}
		@Override
		public boolean addUcenikToTempDB(RegisterUcenikDTO registerUcenikDTO) {
			TempUcenik tempUcenik = new TempUcenik();
			tempUcenik.setImeUcenik(registerUcenikDTO.getImeUcenik());
			tempUcenik.setPrezimeUcenik(registerUcenikDTO.getPrezimeUcenik());
			tempUcenik.setEmail(registerUcenikDTO.getEmail());
			tempUcenik.setLozinka(passwordEncoder.encode(registerUcenikDTO.getLozinka()));

			TempUcenik saved = tempUcenikRepo.save(tempUcenik);

			return saved != null;
		}

		public boolean odobriUcenika(TempUcenik tempUcenik) {
		    Ucenik ucenik = new Ucenik();
		    ucenik.setImeUcenik(tempUcenik.getImeUcenik());
		    ucenik.setPrezimeUcenik(tempUcenik.getPrezimeUcenik());
		    ucenik.setEmail(tempUcenik.getEmail());
		    ucenik.setLozinka(tempUcenik.getLozinka());

		    ucenikRepo.save(ucenik);
		    tempUcenikRepo.delete(tempUcenik);

		    return true;
		}


		public boolean odbaciUcenika(String email) {
		    Optional<TempUcenik> tempUcenik = tempUcenikRepo.findById(email);
		    if (tempUcenik.isPresent()) {
		        tempUcenikRepo.delete(tempUcenik.get());
		        return true;
		    }
		    return false;
		}

		public Optional<TempUcenik> dohvatiZahtjevUcenikaPoId(String email) {
		    return tempUcenikRepo.findById(email);
		}

		
		
		//Djelatnik-----------------------------------------------------------------------------------------------------------------------------------
		
		public List<TempDjelatnik> dohvatiSveZahtjeveDjelatnika() {
		    return tempDjelatnikRepo.findAll();
		}

		@Override
		public boolean addDjelatnikToTempDB(RegisterDjelatnikDTO registerDjelatnikDTO) {
		    TempDjelatnik tempDjelatnik = new TempDjelatnik();
		    tempDjelatnik.setImeDjelatnik(registerDjelatnikDTO.getImeDjelatnik());
		    tempDjelatnik.setPrezimeDjelatnik(registerDjelatnikDTO.getPrezimeDjelatnik());
		    tempDjelatnik.setEmail(registerDjelatnikDTO.getEmail());
		    tempDjelatnik.setLozinka(passwordEncoder.encode(registerDjelatnikDTO.getLozinka()));

		    TempDjelatnik saved = tempDjelatnikRepo.save(tempDjelatnik);

		    return saved != null;
		}

		public boolean odobriDjelatnika(TempDjelatnik tempDjelatnik) {
		    Djelatnik djelatnik = new Djelatnik();
		    djelatnik.setImeDjel(tempDjelatnik.getImeDjelatnik());
		    djelatnik.setPrezimeDjel(tempDjelatnik.getPrezimeDjelatnik());
		    djelatnik.setEmail(tempDjelatnik.getEmail());
		    djelatnik.setLozinka(tempDjelatnik.getLozinka());

		    djelatnikRepo.save(djelatnik);
		    tempDjelatnikRepo.delete(tempDjelatnik);

		    return true;
		}

		public boolean odbaciDjelatnika(String email) {
		    Optional<TempDjelatnik> tempDjelatnik = tempDjelatnikRepo.findById(email);
		    if (tempDjelatnik.isPresent()) {
		        tempDjelatnikRepo.delete(tempDjelatnik.get());
		        return true;
		    }
		    return false;
		}

		public Optional<TempDjelatnik> dohvatiZahtjevDjelatnikaPoId(String email) {
		    return tempDjelatnikRepo.findById(email);
		}
		
		//Ravnatelj---------------------------------------------------------------------------------------------------------------------------------

		public List<TempRavnatelj> dohvatiSveZahtjeveRavnatelja() {
			return tempRavnateljRepo.findAll();
		}

		@Override
		public boolean addRavnateljToTempDB(RegisterRavnateljDTO registerRavnateljDTO) {
			TempRavnatelj tempRavnatelj = new TempRavnatelj();
			tempRavnatelj.setImeRavnatelj(registerRavnateljDTO.getImeRavnatelj());
			tempRavnatelj.setPrezimeRavnatelj(registerRavnateljDTO.getPrezimeRavnatelj());
			tempRavnatelj.setEmail(registerRavnateljDTO.getEmail());
			tempRavnatelj.setLozinka(passwordEncoder.encode(registerRavnateljDTO.getLozinka()));

			TempRavnatelj saved = tempRavnateljRepo.save(tempRavnatelj);

			return saved != null;
		}
		
		public boolean odobriRavnatelja(TempRavnatelj tempRavnatelj) {
			Ravnatelj ravnatelj = new Ravnatelj();
			ravnatelj.setImeRavnatelj(tempRavnatelj.getImeRavnatelj());
			ravnatelj.setPrezimeRavnatelj(tempRavnatelj.getPrezimeRavnatelj());
			ravnatelj.setEmail(tempRavnatelj.getEmail());
			ravnatelj.setLozinka(tempRavnatelj.getLozinka());

			ravnateljRepo.save(ravnatelj);
			tempRavnateljRepo.delete(tempRavnatelj);

			return true;
		}
		
		public boolean odbaciRavnatelja(String email) {
			Optional<TempRavnatelj> tempRavnatelj = tempRavnateljRepo.findById(email);
			if (tempRavnatelj.isPresent()) {
				tempRavnateljRepo.delete(tempRavnatelj.get());
				return true;
			}
			return false;
		}

		public Optional<TempRavnatelj> dohvatiZahtjevRavnateljaPoId(String email) {
			return tempRavnateljRepo.findById(email);
		}

		//Satnicar----------------------------------------------------------------------------------------------------------------------------------------
		
		public List<TempSatnicar> dohvatiSveZahtjeveSatnicara() {
			return tempSatnicarRepo.findAll();
		}

		@Override
		public boolean addSatnicarToTempDB(RegisterSatnicarDTO registerSatnicarDTO) {
			TempSatnicar tempSatnicar = new TempSatnicar();
			tempSatnicar.setImeSatnicar(registerSatnicarDTO.getImeSatnicar());
			tempSatnicar.setPrezimeSatnicar(registerSatnicarDTO.getPrezimeSatnicar());
			tempSatnicar.setEmail(registerSatnicarDTO.getEmail());
			tempSatnicar.setLozinka(passwordEncoder.encode(registerSatnicarDTO.getLozinka()));

			TempSatnicar saved = tempSatnicarRepo.save(tempSatnicar);

			return saved != null;
		}


		public boolean odobriSatnicara(TempSatnicar tempSatnicar) {
			Satnicar satnicar  = new Satnicar();
			satnicar.setImeSatnicar(tempSatnicar.getImeSatnicar());
			satnicar.setPrezimeSatnicar(tempSatnicar.getPrezimeSatnicar());
			satnicar.setEmail(tempSatnicar.getEmail());
			satnicar.setLozinka(tempSatnicar.getLozinka());

			satnicarRepo.save(satnicar);
			tempSatnicarRepo.delete(tempSatnicar);

			return true;
		}


		public boolean odbaciSatnicara(String email) {
			Optional<TempSatnicar> tempSatnicar = tempSatnicarRepo.findById(email);
			if (tempSatnicar.isPresent()) {
				tempSatnicarRepo.delete(tempSatnicar.get());
				return true;
			}
			return false;
		}


		public Optional<TempSatnicar> dohvatiZahtjevSatnicaraPoId(String email) {
			return tempSatnicarRepo.findById(email);
		}

		
}
