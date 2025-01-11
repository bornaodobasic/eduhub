package fer.progi.backend.config;

import fer.progi.backend.dao.NastavnikRepository;
import fer.progi.backend.dao.PredmetRepository;
import fer.progi.backend.dao.UcionicaRepository;
import fer.progi.backend.dao.VrijemeSataRepository;
import fer.progi.backend.dao.RazredRepository;
import fer.progi.backend.dao.SmjerRepository;
import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.domain.Predmet;
import fer.progi.backend.domain.Razred;
import fer.progi.backend.domain.Smjer;
import fer.progi.backend.domain.Ucionica;
import fer.progi.backend.domain.VrijemeSata;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
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


    @PostConstruct
    public void init() {

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


    }
}