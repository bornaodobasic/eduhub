package fer.progi.backend.service.impl;

import fer.progi.backend.dao.RazredRepository;
import fer.progi.backend.dao.SatRepository;
import fer.progi.backend.domain.*;
import fer.progi.backend.rest.RasporedDTO;
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

    @Autowired
    private RazredRepository razredRepository;
    
  

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

    @Override
    public List<Sat> listAll() {
        return satRepo.findAll();
    }

    private void dodijeliNastavnike() {
        List<Razred> sviRazredi = razredService.listAll();
        List<RazredPredmetNastavnik> razredPredmetNastavnik = new ArrayList<>();

        List<Nastavnik> sviNastavnici = nastavnikService.listAll();
        Map<Nastavnik, Integer> opterecenje = new HashMap<>();

        for (Nastavnik nastavnik : sviNastavnici) {
            opterecenje.put(nastavnik, 0);
        }

        for (Razred razred : sviRazredi) {
            List<Predmet> predmetiRazreda = razred.getSmjer().getPredmeti();
            Random random = new Random();

            for (Predmet predmet : predmetiRazreda) {
                List<Nastavnik> moguciNastavnici = predmet.getNastavnici();

                while (true) {
                    Nastavnik moguciNastavnik = moguciNastavnici.get(random.nextInt(moguciNastavnici.size()));
                    if ((opterecenje.get(moguciNastavnik) + predmet.getUkBrSatiTjedno()) <= 16) {
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

        for (Razred r : razredi) {
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

        for (int i = 0; i < 2; i++) {
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

    private void allocateVjeronaukEtika(Razred r, List<Integer> availableHours, ArrayList<Integer> dod1, ArrayList<Integer> dod2, List<Sat> potentialRaspored, Map<Nastavnik, List<Integer>> satiNastavnika) {
        Predmet vjeronauk = r.getSmjer().getPredmeti().stream()
                .filter(p -> p.getNazPredmet().startsWith("Vjeronauk"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Expected exactly one entry for key."));

        Predmet etika = r.getSmjer().getPredmeti().stream()
                .filter(p -> p.getNazPredmet().startsWith("Etika"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Expected exactly one entry for key."));

        RazredPredmetNastavnik rpn1 = rpnService.findRP(r, vjeronauk);
        RazredPredmetNastavnik rpn2 = rpnService.findRP(r, etika);

        Nastavnik nastavnik1 = rpn1.getNastavnik();
        Nastavnik nastavnik2 = rpn2.getNastavnik();

        List<Integer> availableHoursNastavnika1 = new ArrayList<>(satiNastavnika.get(nastavnik1));
        List<Integer> availableHoursNastavnika2 = new ArrayList<>(satiNastavnika.get(nastavnik2));

        List<Integer> filteredList = availableHours.stream()
                .filter(h -> satiNastavnika.get(nastavnik1).contains(h) && satiNastavnika.get(nastavnik2).contains(h) && (!dod1.contains(h) || !dod2.contains(h)))
                .collect(Collectors.toList());

        if (filteredList.isEmpty()) {
            throw new IllegalStateException("No available hours");
        }
        Collections.shuffle(filteredList);
        int hour = filteredList.getFirst();


        availableHours.remove(Integer.valueOf(hour));
        availableHoursNastavnika1.remove(Integer.valueOf(hour));
        availableHoursNastavnika2.remove(Integer.valueOf(hour));
        satiNastavnika.put(nastavnik1, availableHoursNastavnika1);
        satiNastavnika.put(nastavnik2, availableHoursNastavnika2);

        Sat sat1 = new Sat();
        sat1.setRpn(rpn1);
        Ucionica uc = ucionicaService.findById(r.getNazRazred()).orElseThrow();
        sat1.setUcionica(uc);
        VrijemeSata vs = vrijemeSataService.findById(hour).orElseThrow();
        sat1.setVrijemeSata(vs);
        potentialRaspored.add(sat1);

        Sat sat2 = new Sat();
        sat2.setRpn(rpn2);
        String ucionica;
        if (!dod1.contains(hour) && !dod2.contains(hour)) {
            if (Math.random() < 0.5) {
                dod1.add(hour);
                ucionica = "Dodatna_ucionica1";
            } else {
                dod2.add(hour);
                ucionica = "Dodatna_ucionica2";
            }
        } else if (dod1.contains(hour)) {
            dod2.add(hour);
            ucionica = "Dodatna_ucionica2";
        } else {
            dod1.add(hour);
            ucionica = "Dodatna_ucionica1";
        }

        uc = ucionicaService.findById(ucionica).orElseThrow();
        sat2.setUcionica(uc);
        vs = vrijemeSataService.findById(hour).orElseThrow();
        sat2.setVrijemeSata(vs);
        potentialRaspored.add(sat2);
    }

    private void allocateInformatika(Razred r, List<Integer> availableHours, ArrayList<Integer> informatika, List<Sat> potentialRaspored, Map<Nastavnik, List<Integer>> satiNastavnika) {
        List<Integer> potentialHours = new ArrayList<>();

        Predmet informatics = r.getSmjer().getPredmeti().stream()
                .filter(p -> p.getNazPredmet().startsWith("Informatika"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Expected exactly one entry for key."));

        RazredPredmetNastavnik rpn = rpnService.findRP(r, informatics);
        Nastavnik nastavnik = rpn.getNastavnik();

        List<Integer> availableHoursNastavnika = new ArrayList<>(satiNastavnika.get(nastavnik));

        for (int i = 0; i < 2; i++) {
            final int index = i;
            List<Integer> filteredList = availableHours.stream()
                    .filter(h -> !informatika.contains(h) && satiNastavnika.get(nastavnik).contains(h) && (index == 0 || !findDay(h).equals(findDay(potentialHours.getFirst()))))
                    .collect(Collectors.toList());

            if (filteredList.isEmpty()) {
                throw new IllegalStateException("No available hours");
            }
            Collections.shuffle(filteredList);
            int hour = filteredList.getFirst();


            potentialHours.add(hour);
            informatika.add(hour);
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

        for (Razred r : razredi) {
            allocateTjelesni(r, dostupniSatiRazredu.get(r), dvorana1, dvorana2, potentialRaspored, dostupniSatiNastavniku);
        }

        //Generiranje informatike
        ArrayList<Integer> informatika = new ArrayList<>();

        for (Razred r : razredi) {
            if (r.getSmjer().getPredmeti().stream().anyMatch(p -> p.getNazPredmet().startsWith("Informatika"))) {
                allocateInformatika(r, dostupniSatiRazredu.get(r), informatika, potentialRaspored, dostupniSatiNastavniku);
            }
        }

        ArrayList<Integer> dod1 = new ArrayList<>();
        ArrayList<Integer> dod2 = new ArrayList<>();
        for (Razred r : razredi) {
            allocateVjeronaukEtika(r, dostupniSatiRazredu.get(r), dod1, dod2, potentialRaspored, dostupniSatiNastavniku);
        }

        for (Razred r : razredi) {
            allocateRest(r, dostupniSatiRazredu.get(r), potentialRaspored, dostupniSatiNastavniku);
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

    private void allocateRest(Razred r, List<Integer> availableHours, List<Sat> potentialRaspored, Map<Nastavnik, List<Integer>> dostupniSatiNastavniku) {

        int retryCount = 0;
        boolean success = false;
        while (!success && retryCount < 10) {
            try {
                List<Predmet> predmetiRazreda = r.getSmjer().getPredmeti();
                predmetiRazreda.removeIf(p -> p.getNazPredmet().startsWith("Tjelesna"));
                predmetiRazreda.removeIf(p -> p.getNazPredmet().startsWith("Vjeronauk"));
                predmetiRazreda.removeIf(p -> p.getNazPredmet().startsWith("Etika"));
                predmetiRazreda.removeIf(p -> p.getNazPredmet().startsWith("Informatika"));

                List<Integer> satiRazreda = new ArrayList<>(availableHours);
                List<Sat> potencijalniSati = new ArrayList<>();
                Map<Nastavnik, List<Integer>> potencijalniNoviSatiNastavnika = new HashMap<>(dostupniSatiNastavniku);
                predmetiRazreda.sort(Comparator.comparingInt(Predmet::getUkBrSatiTjedno).reversed());

                for (Predmet p : predmetiRazreda) {

                    int brojBlokSati = 0;
                    int brojSlobodnihSati;
                    if (p.getUkBrSatiTjedno() >= 3) {
                        brojBlokSati = p.getUkBrSatiTjedno() / 2;
                        brojSlobodnihSati = p.getUkBrSatiTjedno() - brojBlokSati * 2;
                    } else {
                        brojSlobodnihSati = p.getUkBrSatiTjedno();
                    }
                    RazredPredmetNastavnik rpn = rpnService.findRP(r, p);
                    Nastavnik nastavnik = rpn.getNastavnik();

                    List<Integer> availableHoursNastavnika = new ArrayList<>(potencijalniNoviSatiNastavnika.get(nastavnik));
                    Set<DayOfWeek> daniZaBlokSate = new HashSet<>();

                    List<Integer> krajnji = List.of(7, 14, 21, 28, 35);


                    for (int i = 0; i < brojBlokSati; i++) {
                        List<Integer> potencijalniStartBlokSata = satiRazreda.stream()
                                .filter(h -> !krajnji.contains(h) && satiRazreda.contains(h) && satiRazreda.contains(h + 1) && availableHoursNastavnika.contains(h) && availableHoursNastavnika.contains(h + 1) && !daniZaBlokSate.contains(findDay(h)))
                                .collect(Collectors.toList());


                        if (potencijalniStartBlokSata.isEmpty()) {
                            throw new IllegalStateException("No available hours");
                        }
                        Collections.shuffle(potencijalniStartBlokSata);
                        int hour = potencijalniStartBlokSata.getFirst();


                        daniZaBlokSate.add(findDay(hour));
                        satiRazreda.remove(Integer.valueOf(hour));
                        satiRazreda.remove(Integer.valueOf(hour + 1));
                        availableHoursNastavnika.remove(Integer.valueOf(hour));
                        availableHoursNastavnika.remove(Integer.valueOf(hour + 1));

                        Sat sat1 = new Sat();
                        sat1.setRpn(rpn);
                        Ucionica uc = ucionicaService.findById(r.getNazRazred()).orElseThrow();
                        sat1.setUcionica(uc);
                        VrijemeSata vs = vrijemeSataService.findById(hour).orElseThrow();
                        sat1.setVrijemeSata(vs);
                        potencijalniSati.add(sat1);

                        Sat sat2 = new Sat();
                        sat2.setRpn(rpn);
                        uc = ucionicaService.findById(r.getNazRazred()).orElseThrow();
                        sat2.setUcionica(uc);
                        vs = vrijemeSataService.findById(hour + 1).orElseThrow();
                        sat2.setVrijemeSata(vs);
                        potencijalniSati.add(sat2);

                    }

                    for (int i = 0; i < brojSlobodnihSati; i++) {
                        List<Integer> potencijalnoVrijemeSata = satiRazreda.stream()
                                .filter(h -> satiRazreda.contains(h) && availableHoursNastavnika.contains(h) && !daniZaBlokSate.contains(findDay(h)))
                                .collect(Collectors.toList());


                        if (potencijalnoVrijemeSata.isEmpty()) {
                            throw new IllegalStateException("No available hours");
                        }
                        Collections.shuffle(potencijalnoVrijemeSata);
                        int hour = potencijalnoVrijemeSata.getFirst();


                        daniZaBlokSate.add(findDay(hour));
                        satiRazreda.remove(Integer.valueOf(hour));
                        availableHoursNastavnika.remove(Integer.valueOf(hour));

                        Sat sat = new Sat();
                        sat.setRpn(rpn);
                        Ucionica uc = ucionicaService.findById(r.getNazRazred()).orElseThrow();
                        sat.setUcionica(uc);
                        VrijemeSata vs = vrijemeSataService.findById(hour).orElseThrow();
                        sat.setVrijemeSata(vs);
                        potencijalniSati.add(sat);

                    }


                }
                availableHours = satiRazreda;
                dostupniSatiNastavniku = potencijalniNoviSatiNastavnika;
                potentialRaspored.addAll(potencijalniSati);

                success = true;
                System.out.println("USPJESNO");
            } catch (IllegalStateException e) {
                retryCount++;
                System.out.println("NEUSPJESNO - Attempt " + retryCount + ": " + r.getNazRazred() + " " + e.getMessage());
                if (retryCount >= 10) {
                    System.out.println("Maximum retry attempts reached, restarting function...");
                    retryCount = 0;
                }
            }
        }

    }

    @Override
    public void dodijeliRazrednike() {

        List<Nastavnik> sviNastavnici = new ArrayList<>(nastavnikService.findAllNastavniks());
        List<Razred> sviRazredi = razredService.listAll();

        for (Razred r : sviRazredi) {
            List<RazredPredmetNastavnik> rpnLista = rpnService.findByRazred(r);

            List<Nastavnik> potencijalniRazrednici = rpnLista.stream()
                    .map(RazredPredmetNastavnik::getNastavnik)
                    .filter(sviNastavnici::contains)
                    .collect(Collectors.toList());

            Collections.shuffle(potencijalniRazrednici);
            Nastavnik razrednik = potencijalniRazrednici.getFirst();

            Razred raz = razredRepository.getByNazRazred(r.getNazRazred());
            raz.setRazrednik(razrednik);
            razredRepository.save(raz);
            sviNastavnici.remove(razrednik);
        }


    }

	@Override
	public List<RasporedDTO> getRasporedZaNastavnika(String email) {
		// TODO Auto-generated method stub
		return null;
	}

   


}
