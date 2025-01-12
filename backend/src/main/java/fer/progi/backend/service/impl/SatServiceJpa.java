package fer.progi.backend.service.impl;

import fer.progi.backend.dao.SatRepository;
import fer.progi.backend.domain.*;
import fer.progi.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class SatServiceJpa implements SatService {

    @Autowired
    private RazredPredmetNastavnikService rpnService;

    @Autowired
    private VrijemeSataService vrijemeSataService;

    @Autowired
    private SatRepository satRepo;

    @Autowired
    private PredmetService predmetService;

    @Autowired
    private SmjerService smjerService;

    @Autowired
    private RazredService razredService;

    @Autowired
    private UcionicaService ucionicaService;

    @Autowired
    private NastavnikService nastavnikService;

    @Override
    public DayOfWeek findDay(int sat) {
        if (sat >= 1 && sat <= 7) {
            return DayOfWeek.MONDAY;
        } else if (sat >= 8 && sat <= 14) {
            return DayOfWeek.TUESDAY;
        } else if (sat >= 15 && sat <= 21) {
            return DayOfWeek.WEDNESDAY;
        } else if (sat >= 22 && sat <= 28) {
            return DayOfWeek.THURSDAY;
        } else {
            return DayOfWeek.FRIDAY;
        }
    }

    private void dodijeliNastavnike() {
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

        rpnService.saveAll(razredPredmetNastavnik);


    }

    private Map<Razred, Integer> calculateSatiPoRazredu(List<Razred> razredi) {
        Map<Razred, Integer> satiPoRazredu = new HashMap<>();
        for (Razred r : razredi) {
            int sum = r.getSmjer().getPredmeti().stream()
                    .mapToInt(Predmet::getUkBrSatiTjedno)
                    .sum();
            satiPoRazredu.put(r, sum - 1);
        }
        return satiPoRazredu;
    }

    private Map<Razred, List<Integer>> initAvailableHours(List<Razred> razredi, Map<Razred, Integer> satiPoRazredu) {
        Map<Razred, List<Integer>> dostupniSati = new HashMap<>();
        List<Integer> sviSati = IntStream.rangeClosed(1, 35).boxed().toList();
        List<Integer> krajnji = new ArrayList<>(List.of(1, 7, 8, 14, 15, 21, 22, 28, 29, 35));

        for(Razred r : razredi) {
            List<Integer> availableHours = new ArrayList<>(sviSati);
            int removeCount = 35 - satiPoRazredu.get(r);
            Collections.shuffle(krajnji);
            availableHours.removeAll(krajnji.subList(0, Math.min(removeCount, krajnji.size())));
            dostupniSati.put(r, availableHours);
        }
        return dostupniSati;
    }

    private Map<Nastavnik, List<Integer>> initAvailableNastavnik(List<Nastavnik> nastavnici) {
        Map<Nastavnik, List<Integer>> dostupniSati = new HashMap<>();
        for (Nastavnik n : nastavnici) {
            dostupniSati.put(n, IntStream.rangeClosed(1, 35).boxed().toList());
        }
        return dostupniSati;
    }

    private void allocateTjelesni(Razred r, List<Integer> availableHours, ArrayList<Integer> dvorana1, ArrayList<Integer> dvorana2, List<Sat> potentialRaspored, Map<Nastavnik, List<Integer>> satiNastavnika) {
        List<Integer> potentialHours = new ArrayList<>();

        String dvorana = r.getNazRazred().startsWith("1") || r.getNazRazred().startsWith("2") ? "Dvorana1" : "Dvorana2";
        List<Integer> usedHours = dvorana.equals("Dvorana1") ? dvorana1 : dvorana2;

        Predmet tjelesni = r.getSmjer().getPredmeti().stream()
                .filter(p -> p.getNazPredmet().startsWith("Tjelesna"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Expected exactly one entry for key."));

        RazredPredmetNastavnik rpn = rpnService.findRP(r, tjelesni);
        Nastavnik nastavnik = rpn.getNastavnik();

        List<Integer> availableHoursNastavnika = new ArrayList<>(satiNastavnika.get(nastavnik));

        for(int i = 0; i < 2; i++) {
            final int index = i;
            List<Integer> filteredList = availableHours.stream()
                    .filter(h -> !usedHours.contains(h) && satiNastavnika.get(nastavnik).contains(h) && (index == 0 || !findDay(h).equals(findDay(potentialHours.getFirst()))))
                    .collect(Collectors.toList());

            if (filteredList.isEmpty()) {
                throw new IllegalStateException("No available hours");
            }
            Collections.shuffle(filteredList);
            int hour = filteredList.getFirst();


            potentialHours.add(hour);
            usedHours.add(hour);
            availableHours.remove(Integer.valueOf(hour));
            availableHoursNastavnika.remove(Integer.valueOf(hour));
            satiNastavnika.put(nastavnik, availableHoursNastavnika);
        }

        List<Sat> satList = potentialHours.stream().map(hour -> {
            Sat sat = new Sat();
            sat.setRpn(rpn);
            Ucionica uc = ucionicaService.findById(dvorana).orElseThrow();
            sat.setUcionica(uc);
            VrijemeSata vs = vrijemeSataService.findById(hour).orElseThrow();
            sat.setVrijemeSata(vs);
            return sat;
        }).toList();
        potentialRaspored.addAll(satList);
    }

    private void allocateInformatika(Razred r, List<Integer> availableHours, ArrayList<Integer> informatika, List<Sat> potentialRaspored, Map<Nastavnik, List<Integer>> satiNastavnika) {
        List<Integer> potentialHours = new ArrayList<>();
        List<Integer> usedHours = informatika;

        Predmet informatics = r.getSmjer().getPredmeti().stream()
                .filter(p -> p.getNazPredmet().startsWith("Informatika"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Expected exactly one entry for key."));

        RazredPredmetNastavnik rpn = rpnService.findRP(r, informatics);
        Nastavnik nastavnik = rpn.getNastavnik();

        List<Integer> availableHoursNastavnika = new ArrayList<>(satiNastavnika.get(nastavnik));

        for(int i = 0; i < 2; i++) {
            final int index = i;
            List<Integer> filteredList = availableHours.stream()
                    .filter(h -> !usedHours.contains(h) && satiNastavnika.get(nastavnik).contains(h) && (index == 0 || !findDay(h).equals(findDay(potentialHours.getFirst()))))
                    .collect(Collectors.toList());

            if (filteredList.isEmpty()) {
                throw new IllegalStateException("No available hours");
            }
            Collections.shuffle(filteredList);
            int hour = filteredList.getFirst();


            potentialHours.add(hour);
            usedHours.add(hour);
            availableHours.remove(Integer.valueOf(hour));
            availableHoursNastavnika.remove(Integer.valueOf(hour));
            satiNastavnika.put(nastavnik, availableHoursNastavnika);
        }

        List<Sat> satList = potentialHours.stream().map(hour -> {
            Sat sat = new Sat();
            sat.setRpn(rpn);
            Ucionica uc = ucionicaService.findById("Informatika").orElseThrow();
            sat.setUcionica(uc);
            VrijemeSata vs = vrijemeSataService.findById(hour).orElseThrow();
            sat.setVrijemeSata(vs);
            return sat;
        }).toList();
        potentialRaspored.addAll(satList);
    }


    @Override
    public void generirajRaspored() {
        dodijeliNastavnike();

        List<Razred> razredi = razredService.listAll();
        Map<Razred, Integer> satiPoRazredu = calculateSatiPoRazredu(razredi);
        Map<Razred, List<Integer>> dostupniSatiRazredu = initAvailableHours(razredi, satiPoRazredu);

        List<Nastavnik> nastavnici = nastavnikService.listAll();
        Map<Nastavnik, List<Integer>> dostupniSatiNastavniku = initAvailableNastavnik(nastavnici);

        List<Sat> potentialRaspored = new ArrayList<>();


        //Generiranje tjelesnog
        ArrayList<Integer> dvorana1 = new ArrayList<>();
        ArrayList<Integer> dvorana2 = new ArrayList<>();

        for(Razred r : razredi) {
            allocateTjelesni(r, dostupniSatiRazredu.get(r), dvorana1, dvorana2, potentialRaspored, dostupniSatiNastavniku);
        }

        //Generiranje informatike
        ArrayList<Integer> informatika = new ArrayList<>();

        for(Razred r : razredi) {
            if (r.getSmjer().getPredmeti().stream().anyMatch(p -> p.getNazPredmet().startsWith("Informatika"))) {
                allocateInformatika(r, dostupniSatiRazredu.get(r), informatika, potentialRaspored, dostupniSatiNastavniku);
            }
        }
        satRepo.saveAll(potentialRaspored);

        /*
        for(Map.Entry<Razred, List<Integer>> entry : dostupniSatiRazredu.entrySet()) {
            String key = entry.getKey().getNazRazred();
            List<Integer> value = entry.getValue();

            System.out.println("Razred: " + key);
            System.out.println("Sati: " + value);
        }
         */
    }
}
