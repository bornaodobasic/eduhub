package fer.progi.backend.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fer.progi.backend.dao.ObavijestRepository;
import fer.progi.backend.dao.PredmetRepository;
import fer.progi.backend.domain.Obavijest;
import fer.progi.backend.domain.Predmet;
import fer.progi.backend.service.ObavijestService;

@Service
public class ObavijestServiceJpa  implements ObavijestService{
	
	@Autowired
	private ObavijestRepository obavijestRepo;
	
	@Autowired
	private PredmetRepository predmetRepo;

	@Override
	public void dodajObavijest(String naslovObavijest, String sadrzajObavijest, Date datumObavijest, String nazPredmet,
							   String imeNastavnik, String prezimeNastavnik, String adresaLokacija,
							   String gradLokacija, String drzavaLokacija) {


		if (naslovObavijest == null || naslovObavijest.isEmpty()) {
			throw new IllegalArgumentException("Naslov obavijesti ne smije biti prazan.");
		}

		if (sadrzajObavijest == null || sadrzajObavijest.isEmpty()) {
			throw new IllegalArgumentException("Sadržaj obavijesti ne smije biti prazan.");
		}

		Obavijest obavijest = new Obavijest();
		obavijest.setNaslovObavijest(naslovObavijest);
		obavijest.setSadrzajObavijest(sadrzajObavijest);
		obavijest.setDatumObavijest(datumObavijest);


		if (nazPredmet != null) {
			Predmet predmet = predmetRepo.findByNazPredmet(nazPredmet);
			obavijest.setPredmet(predmet);
		}


		obavijest.setImeNastavnik(imeNastavnik);
		obavijest.setPrezimeNastavnik(prezimeNastavnik);


		obavijest.setAdresaLokacija(adresaLokacija);
		obavijest.setGradLokacija(gradLokacija);
		obavijest.setDrzavaLoakcija(drzavaLokacija);


		obavijestRepo.save(obavijest);
	}


	@Override
	public void obrisiObavijest(int sifObavijest) {
		obavijestRepo.deleteById(sifObavijest);
		
	}

	@Override
	public List<Obavijest> prikaziObavijestiZaPredmet(String nazPredmet) {
		return obavijestRepo.findByPredmetNazPredmet(nazPredmet);
	}

	@Override
	public List<Obavijest> prikaziOpceObavijesti() {
		return obavijestRepo.findByPredmetIsNull();
	}


}
