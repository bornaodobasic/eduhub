package fer.progi.backend.service;

import java.util.List;
import java.util.Optional;

import fer.progi.backend.domain.Admin;
import fer.progi.backend.domain.TempNastavnik;
import fer.progi.backend.rest.RegisterNastavnikDTO;


public interface AdminService {
	
	List<Admin> listAll();
	
	Admin dodajAdmin (Admin admin);

	boolean addNastavnikToTempDB(RegisterNastavnikDTO registerNastavnikDTO);

	List<TempNastavnik> dohvatiSveZahtjeveNastavnika();

	boolean odobriNastavnika(TempNastavnik tempNastavnik);

	boolean odbaciNastavnika(String email);
	Optional<TempNastavnik> dohvatiZahtjevNastavnikaPoId(String email);

}
