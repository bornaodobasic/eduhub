package fer.progi.backend.service.impl;

import fer.progi.backend.dao.RazredPredmetNastavnikRepository;
import fer.progi.backend.domain.*;
import fer.progi.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RazredPredmetNastavnikServiceJpa implements RazredPredmetNastavnikService {

    @Autowired
    private RazredPredmetNastavnikRepository rpnRepo;

    @Autowired
    private RazredService razredService;

    @Autowired
    private NastavnikService nastavnikService;

    @Autowired
    private PredmetService predmetService;

    @Autowired
    private SmjerService smjerService;

    @Override
    public List<RazredPredmetNastavnik> listAll() {
        return rpnRepo.findAll();
    }

    public void dodijeliNastavnike() {
        List<Razred> sviRazredi = razredService.listAll();
        List<RazredPredmetNastavnik> razredPredmetNastavnik = new ArrayList<>();

        List<Nastavnik> sviNastavnici = nastavnikService.listAll();
        Map<Nastavnik, Integer> opterecenje = new HashMap<>();

        for(Nastavnik nastavnik : sviNastavnici) {
            opterecenje.put(nastavnik, 0);
        }

        for(Razred razred : sviRazredi) {
            List<Predmet> predmetiRazreda = razred.getSmjer().getPredmeti();
            Random random = new Random();

            for(Predmet predmet : predmetiRazreda) {
                List<Nastavnik> moguciNastavnici = predmet.getNastavnici();

                while (true) {
                    Nastavnik moguciNastavnik = moguciNastavnici.get(random.nextInt(moguciNastavnici.size()));
                    if((opterecenje.get(moguciNastavnik) + predmet.getUkBrSatiTjedno()) <= 16) {
                        RazredPredmetNastavnik rpn = new RazredPredmetNastavnik();
                        rpn.setRazred(razred);
                        rpn.setPredmet(predmet);
                        rpn.setNastavnik(moguciNastavnik);

                        razredPredmetNastavnik.add(rpn);
                        opterecenje.put(moguciNastavnik, opterecenje.get(moguciNastavnik) + predmet.getUkBrSatiTjedno());
                        break;
                    } else {
                        moguciNastavnici.remove(moguciNastavnik);
                    }
                }

            }

        }

        rpnRepo.saveAll(razredPredmetNastavnik);


    }

    public void sati() {
        List<Smjer> smjerovi = smjerService.listAll();
        for(Smjer smjer : smjerovi) {
            List<Predmet> predmet = smjer.getPredmeti();
            int sum = 0;
            for(Predmet p : predmet) {
                sum += p.getUkBrSatiTjedno();
            }
            System.out.println(smjer.getNazivSmjer() + " " + sum);
        }
    }

    public RazredPredmetNastavnik findRP(Razred razred, Predmet predmet) {
        return rpnRepo.findByRazredAndPredmet(razred, predmet);
    }

    @Override
    public void saveAll(List<RazredPredmetNastavnik> rpn) {
        rpnRepo.saveAll(rpn);
    }

    @Override
    public List<RazredPredmetNastavnik> findByNastavnik(Nastavnik nastavnik) {
        return rpnRepo.findByNastavnik(nastavnik);
    }

}
