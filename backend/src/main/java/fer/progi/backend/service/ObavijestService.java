package fer.progi.backend.service;

import java.util.Date;
import java.util.List;

import fer.progi.backend.domain.Obavijest;

public interface ObavijestService {

	public void dodajObavijest(
	        String naslovObavijest,
	        String sadrzajObavijest,
	        Date datumObavijest,
	        String nazPredmet,
	        String imeNastavnik,
	        String prezimeNastavnik,
	        String adresaLokacija,
	        String gradLokacija,
	        String drzavaLokacija);
	
	void obrisiObavijest(int sifObavijest);
	List<Obavijest> prikaziObavijestiZaPredmet(String nazPredmet);
	List<Obavijest> prikaziOpceObavijesti();
	
}
