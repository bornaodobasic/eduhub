package fer.progi.backend.config;

import fer.progi.backend.dao.*;
import fer.progi.backend.domain.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class DatabaseInitializer {

    @Autowired
    private SmjerRepository smjerRepository;

    @Autowired
    private RazredRepository razredRepository;

    @Autowired
    private PredmetRepository predmetRepository;

    @Autowired
    private UcionicaRepository ucionicaRepository;

    @Autowired
    private NastavnikRepository nastavnikRepository;

    @Autowired
    private VrijemeSataRepository vrijemeSataRepository;

    @Autowired
    private AktivnostRepository aktivnostRepository;

    @Autowired
    private UcenikRepository ucenikRepository;

    @Autowired
    private RavnateljRepository ravnateljRepository;

    @Autowired
    private SatnicarRepository satnicarRepository;

    @Autowired
    private DjelatnikRepository djelatnikRepository;
    
    @Autowired
	private ChatGroupRepository chatGroupRepository;


    @PostConstruct
    public void init() {

    	try {
    	    BufferedReader reader = new BufferedReader(
    	            new InputStreamReader(getClass().getResourceAsStream("/db/djelatnici.csv"))
    	    );

    	    List<String> lines = reader.lines().skip(1).collect(Collectors.toList());

    	    if (lines.size() < 3) {
    	        throw new IllegalArgumentException("Djelatnici CSV ne sadrži dovoljno podataka.");
    	    }

    	    // Ravnatelj
    	    String[] ravnateljFields = lines.get(0).split(",");
    	    String ravnateljEmail = ravnateljFields[2].trim();
    	    if (!ravnateljRepository.existsByEmail(ravnateljEmail)) {
    	        Ravnatelj ravnatelj = new Ravnatelj();
    	        ravnatelj.setImeRavnatelj(ravnateljFields[0].trim());
    	        ravnatelj.setPrezimeRavnatelj(ravnateljFields[1].trim());
    	        ravnatelj.setEmail(ravnateljEmail);
    	        ravnateljRepository.save(ravnatelj);
    	    }

    	    // Satnicar
    	    String[] satnicarFields = lines.get(1).split(",");
    	    String satnicarEmail = satnicarFields[2].trim();
    	    if (!satnicarRepository.existsByEmail(satnicarEmail)) {
    	        Satnicar satnicar = new Satnicar();
    	        satnicar.setImeSatnicar(satnicarFields[0].trim());
    	        satnicar.setPrezimeSatnicar(satnicarFields[1].trim());
    	        satnicar.setEmail(satnicarEmail);
    	        satnicarRepository.save(satnicar);
    	    }

    	    // Djelatnici
    	    List<Djelatnik> djelatnici = lines.stream()
    	            .skip(2)
    	            .map(line -> {
    	                String[] fields = line.split(",");
    	                String email = fields[2].trim();
    	                if (!djelatnikRepository.existsByEmail(email)) { // Provjera da ne postoji
    	                    Djelatnik djelatnik = new Djelatnik();
    	                    djelatnik.setImeDjel(fields[0].trim());
    	                    djelatnik.setPrezimeDjel(fields[1].trim());
    	                    djelatnik.setEmail(email);
    	                    return djelatnik;
    	                }
    	                return null; // Ako već postoji, vraća null
    	            })
    	            .filter(Objects::nonNull) // Uklanja null vrijednosti
    	            .collect(Collectors.toList());

    	    if (!djelatnici.isEmpty()) {
    	        djelatnikRepository.saveAll(djelatnici);
    	    }
    	} catch (Exception e) {
    	    e.printStackTrace();
    	}



        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getClass().getResourceAsStream("/db/aktivnosti.csv"))
            );

            List<Aktivnost> aktivnosti = reader.lines()
                    .skip(1)
                    .map(line -> {
                        String oznAktivnosti = line.trim();
                        if (aktivnostRepository.existsByOznAktivnost(oznAktivnosti)) {
                            return null;
                        }

                        Aktivnost aktivnost = new Aktivnost();
                        aktivnost.setOznAktivnost(oznAktivnosti);
                        return aktivnost;
                    })
                    .filter(aktivnost -> aktivnost != null)
                    .collect(Collectors.toList());

            if (!aktivnosti.isEmpty()) {
                aktivnostRepository.saveAll(aktivnosti);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getClass().getResourceAsStream("/db/vrijemesata.csv"))
            );
            List<VrijemeSata> vremena = reader.lines()
                    .skip(1)
                    .map(line -> {
                        String[] fields = line.split(",");
                        String dan = fields[0].trim();
                        String pocetak = fields[1].trim();
                        String kraj = fields[2].trim();

                        DayOfWeek dayOfWeek = DayOfWeek.valueOf(dan.toUpperCase());
                        LocalTime start = LocalTime.parse(pocetak);
                        LocalTime end = LocalTime.parse(kraj);

                        if (vrijemeSataRepository.existsByDanAndPocetakSataAndKrajSata(dayOfWeek, start, end)) {
                            return null;
                        }

                        VrijemeSata vrijemeSata = new VrijemeSata();
                        vrijemeSata.setDan(dayOfWeek);
                        vrijemeSata.setPocetakSata(start);
                        vrijemeSata.setKrajSata(end);
                        return vrijemeSata;
                    })
                    .filter(vrijemeSata -> vrijemeSata != null)
                    .collect(Collectors.toList());

            if (!vremena.isEmpty()) {vrijemeSataRepository.saveAll(vremena);}


        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getClass().getResourceAsStream("/db/ucionica.csv"))
            );

            List<Ucionica> ucionice = reader.lines()
                    .skip(1)
                    .map(line -> {
                        String[] fields = line.split(",");
                        String oznaka = fields[0].trim();
                        String kapacitet = fields[1].trim();

                        if (ucionicaRepository.existsByOznakaUc(oznaka)) {
                            return null;
                        }
                        Ucionica ucionica = new Ucionica();
                        ucionica.setOznakaUc(oznaka);
                        ucionica.setKapacitet(Integer.parseInt(kapacitet));
                        return ucionica;
                    })
                    .filter(ucionica -> ucionica != null)
                    .collect(Collectors.toList());


            ucionicaRepository.saveAll(ucionice);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            List<Smjer> smjerovi = Files.lines(Paths.get(getClass().getResource("/db/smjer.csv").toURI()))
                    .skip(1)
                    .map(line -> {
                        String[] fields = line.split(",");
                        return new Smjer(fields[0]);
                    })
                    .filter(smjer -> !smjerRepository.existsByNazivSmjer(smjer.getNazivSmjer()))
                    .collect(Collectors.toList());

            smjerRepository.saveAll(smjerovi);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getClass().getResourceAsStream("/db/razred.csv"))
            );

            List<Razred> razredi = reader.lines()
                    .skip(1)
                    .map(line -> {
                        String[] fields = line.split(",");
                        String nazRazred = fields[0].trim();
                        String nazivSmjer = fields[1].trim();

                        Smjer smjer = smjerRepository.findByNazivSmjer(nazivSmjer)
                                .orElseThrow(() -> new IllegalArgumentException("Smjer nije pronađen: " + nazivSmjer));

                        if (razredRepository.existsByNazRazredAndSmjer(nazRazred, smjer)) {
                            return null;
                        }
                        Razred razred = new Razred();
                        razred.setNazRazred(nazRazred);
                        razred.setSmjer(smjer);
                        return razred;
                    })
                    .filter(razred -> razred != null)
                    .collect(Collectors.toList());

            razredRepository.saveAll(razredi);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getClass().getResourceAsStream("/db/predmet.csv"))
            );

            List<Predmet> predmeti = reader.lines()
                    .skip(1)
                    .map(line -> {
                        String[] fields = line.split(",");
                        String nazPredmet = fields[0].trim();
                        int brSatiTjedno = Integer.parseInt(fields[1].trim());
                        String nazivSmjer = fields[2].trim();

                        Smjer smjer = smjerRepository.findByNazivSmjer(nazivSmjer)
                                .orElseThrow(() -> new IllegalArgumentException("Smjer nije pronađen: " + nazivSmjer));

                        if (predmetRepository.existsByNazPredmetAndSmjer(nazPredmet, smjer)) {
                            return null;
                        }

                        Predmet predmet = new Predmet();
                        predmet.setNazPredmet(nazPredmet);
                        predmet.setUkBrSatiTjedno(brSatiTjedno);
                        predmet.setSmjer(smjer);
                        return predmet;
                    })
                    .filter(predmet -> predmet != null)
                    .collect(Collectors.toList());

            predmetRepository.saveAll(predmeti);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getClass().getResourceAsStream("/db/nastavnik.csv"))
            );
            List<Nastavnik> nastavnici = reader.lines()
                    .skip(1)
                    .map(line -> {
                        String[] fields = line.split(",");
                        String email = fields[2].trim();

                        if (nastavnikRepository.existsByEmail(email)) {
                            return null;
                        }

                        String ime = fields[0].trim();
                        String prezime = fields[1].trim();
                        String[] predmetiLista = fields[3].trim().split(";");

                        Nastavnik nastavnik = new Nastavnik();
                        nastavnik.setImeNastavnik(ime);
                        nastavnik.setPrezimeNastavnik(prezime);
                        nastavnik.setEmail(email);

                        List<Predmet> predmeti = Arrays.stream(predmetiLista)
                                .map(id -> predmetRepository.findByNazPredmet(id.trim()))
                                .filter(predmet -> predmet != null)
                                .collect(Collectors.toList());
                        nastavnik.setPredmeti(predmeti);

                        predmeti.forEach(predmet -> predmet.getNastavnici().add(nastavnik));

                        return nastavnik;
                    })
                    .filter(nastavnik -> nastavnik != null)
                    .collect(Collectors.toList());

            nastavnikRepository.saveAll(nastavnici);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getClass().getResourceAsStream("/db/ucenik.csv"))
            );
            List<Ucenik> ucenici = reader.lines()
                    .skip(1)
                    .map(line -> {
                        String[] fields = line.split(",");
                        String email = fields[6].trim();

                        if (ucenikRepository.existsByEmail(email)) {
                            return null;
                        }

                        String ime = fields[0].trim();
                        String prezime = fields[1].trim();
                        String oib = fields[2].trim();
                        String datumRodenja = fields[3].trim();
                        Razred razred = razredRepository.getByNazRazred(fields[4].trim());
                        String spol = fields[5].trim();
                        boolean vjeronauk = Boolean.parseBoolean(fields[7].trim());

                        boolean hasAktivnost = false;
                        Ucenik ucenik = new Ucenik();

                        if (fields.length > 8) {
                            String[] listaAktivnosti = fields[8].trim().split(";");
                            List<Aktivnost> aktivnosti = Arrays.stream(listaAktivnosti)
                                    .map(id -> aktivnostRepository.findByOznAktivnost(id.trim()))
                                    .filter(akt -> akt != null)
                                    .collect(Collectors.toList());
                            ucenik.setAktivnosti(aktivnosti);
                            aktivnosti.forEach(akt -> akt.getUcenici().add(ucenik));
                        }

                        ucenik.setImeUcenik(ime);
                        ucenik.setPrezimeUcenik(prezime);
                        ucenik.setOib(oib);
                        ucenik.setDatumRodenja(datumRodenja);
                        ucenik.setRazred(razred);
                        ucenik.setSpol(spol);
                        ucenik.setVjeronauk(vjeronauk);
                        ucenik.setEmail(email);

                        return ucenik;
                    })
                    .filter(ucenik -> ucenik != null)
                    .collect(Collectors.toList());

            ucenikRepository.saveAll(ucenici);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
    
            List<Razred> razredi = razredRepository.findAll();
            List<Ucenik> ucenici = ucenikRepository.findAll();

            for (Razred razred : razredi) {

                String imeGrupe = razred.getNazRazred();
                if (chatGroupRepository.existsByImeGrupe(imeGrupe)) {
                    continue; 
                }

                ChatGroup chatGroup = new ChatGroup();
                chatGroup.setImeGrupe(imeGrupe);

                List<String> clanovi = new ArrayList<>();
                for(Ucenik u : ucenici) {
                	if(u.getRazred().getNazRazred().equals(razred.getNazRazred())) {
                		clanovi.add(u.getEmail());
                	}
                	
                }

                if (razred.getRazrednik() != null) {
                    clanovi.add(razred.getRazrednik().getEmail());
                }

                chatGroup.setClanovi(clanovi);

                chatGroupRepository.save(chatGroup);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Dogodila se pogreška prilikom kreiranja chat grupa: " + e.getMessage());
        }




    }
}