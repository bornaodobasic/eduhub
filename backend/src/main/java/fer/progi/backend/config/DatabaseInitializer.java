package fer.progi.backend.config;

import fer.progi.backend.dao.PredmetRepository;
import fer.progi.backend.dao.UcionicaRepository;
import fer.progi.backend.dao.RazredRepository;
import fer.progi.backend.dao.SmjerRepository;
import fer.progi.backend.domain.Predmet;
import fer.progi.backend.domain.Razred;
import fer.progi.backend.domain.Smjer;
import fer.progi.backend.domain.Ucionica;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
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


    @PostConstruct
    public void init() {

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
                        //Integer brSatiTjedno = Integer.parseInt(fields[1].trim());
                        String brSatiTjedno = fields[1].trim();
                        String nazivSmjer = fields[2].trim();

                        Smjer smjer = smjerRepository.findByNazivSmjer(nazivSmjer)
                                .orElseThrow(() -> new IllegalArgumentException("Smjer nije pronađen: " + nazivSmjer));

                        if (predmetRepository.existsByNazPredmet(nazPredmet)) {
                            return null;
                        }
                        Predmet predmet = new Predmet();
                        predmet.setNazPredmet(nazPredmet);
                        predmet.setUkBrSatiTjedno(Integer.parseInt(brSatiTjedno));
                        predmet.setSmjer(smjer);

                        return predmet;
                    })
                    .filter(predmet -> predmet != null)
                    .collect(Collectors.toList());

            predmetRepository.saveAll(predmeti);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
