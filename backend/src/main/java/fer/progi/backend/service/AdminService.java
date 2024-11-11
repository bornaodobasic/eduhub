package fer.progi.backend.service;

import java.util.List;
import java.util.Optional;

import fer.progi.backend.domain.Admin;
import fer.progi.backend.domain.TempDjelatnik;
import fer.progi.backend.domain.TempNastavnik;
import fer.progi.backend.domain.TempUcenik;
import fer.progi.backend.rest.RegisterDjelatnikDTO;
import fer.progi.backend.rest.RegisterNastavnikDTO;
import fer.progi.backend.rest.RegisterUcenikDTO;


public interface AdminService {
	
	List<Admin> listAll();
	
	Admin dodajAdmin (Admin admin);

	boolean addNastavnikToTempDB(RegisterNastavnikDTO registerNastavnikDTO);

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
	
	boolean addDjelatnikToTempDB(RegisterDjelatnikDTO registerDjelatnikDTO);
	
	List<TempDjelatnik> dohvatiSveZahtjeveDjelatnika();
	
	boolean odobriDjelatnika(TempDjelatnik tempDjelatnik);

	boolean odbaciDjelatnika(String email);
	
	Optional<TempDjelatnik> dohvatiZahtjevDjelatnikaPoId(String email);

}
