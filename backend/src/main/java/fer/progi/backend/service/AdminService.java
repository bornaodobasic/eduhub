package fer.progi.backend.service;

import java.util.List;

import java.util.Optional;

import fer.progi.backend.domain.Admin;
import fer.progi.backend.domain.TempAdmin;
import fer.progi.backend.domain.TempDjelatnik;
import fer.progi.backend.domain.TempNastavnik;
import fer.progi.backend.domain.TempRavnatelj;
import fer.progi.backend.domain.TempSatnicar;
import fer.progi.backend.domain.TempUcenik;
import fer.progi.backend.rest.RegisterKorisnikDTO;
import fer.progi.backend.rest.RegisterUcenikDTO;


public interface AdminService {
	
	List<Admin> listAll();
	
	Admin dodajAdmin (Admin admin);
	
	boolean addAdminToTempDB(RegisterKorisnikDTO registerKorisnikDTO);

	List<TempAdmin> dohvatiSveZahtjeveAdmina();
	
	boolean odobriAdmina(TempAdmin tempAdmin);

	boolean odbaciAdmina(String email);
	
	Optional<TempAdmin> dohvatiZahtjevAdminaPoId(String email);
	
	//Nastavnik--------------------------------------------------------------------------------------

	boolean addNastavnikToTempDB(RegisterKorisnikDTO registerKorisnikDTO);

	List<TempNastavnik> dohvatiSveZahtjeveNastavnika();

	boolean odobriNastavnika(TempNastavnik tempNastavnik);

	boolean odbaciNastavnika(String email);
	
	Optional<TempNastavnik> dohvatiZahtjevNastavnikaPoId(String email);
	
	//Ucenik-----------------------------------------------------------------------------------------
	
	boolean addUcenikToTempDB(RegisterUcenikDTO registerUcenikDTO);

	List<TempUcenik> dohvatiSveZahtjeveUcenika();
	
	boolean odobriUcenika(TempUcenik tempUcenik);

	boolean odbaciUcenika(String email);
	
	Optional<TempUcenik> dohvatiZahtjevUcenikaPoId(String email);
	
	//Djelatnik------------------------------------------------------------------------------------------
	
	boolean addDjelatnikToTempDB(RegisterKorisnikDTO registerKorisnikDTO);
	
	List<TempDjelatnik> dohvatiSveZahtjeveDjelatnika();
	
	boolean odobriDjelatnika(TempDjelatnik tempDjelatnik);

	boolean odbaciDjelatnika(String email);
	
	Optional<TempDjelatnik> dohvatiZahtjevDjelatnikaPoId(String email);
	
	//Ravnatelj-------------------------------------------------------------------------------------------
	
	boolean addRavnateljToTempDB(RegisterKorisnikDTO registerKorisnikDTO);
	
	List<TempRavnatelj> dohvatiSveZahtjeveRavnatelja();
	
	boolean odobriRavnatelja(TempRavnatelj tempRavnatelj);

	boolean odbaciRavnatelja(String email);
	
	//Satnicar---------------------------------------------------------------------------------------------
	
	boolean addSatnicarToTempDB(RegisterKorisnikDTO registerKorisnikDTO);
	
	List<TempSatnicar> dohvatiSveZahtjeveSatnicara();
	
	boolean odobriSatnicara(TempSatnicar tempSatnicar);
	
	boolean odbaciSatnicara(String email);

	Optional<TempRavnatelj> dohvatiZahtjevRavnateljaPoId(String email);

	Optional<TempSatnicar> dohvatiZahtjevSatnicaraPoId(String email);

}
