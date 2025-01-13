
package fer.progi.backend.rest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fer.progi.backend.domain.Ravnatelj;
import fer.progi.backend.domain.Ucionica;
import fer.progi.backend.service.RavnateljService;
import fer.progi.backend.service.UcionicaService;


@RestController
@RequestMapping("/api/ravnatelj")
@PreAuthorize("hasAuthority('Ravnatelj')")
public class RavnateljController {

    @Autowired
    private RavnateljService RavnateljService;

    @Autowired
    private UcionicaService ucionicaService;

    @GetMapping("")
    public List<Ravnatelj> listRavnatelj() {
        return RavnateljService.listAll();
    }

    @PostMapping("")
    public Ravnatelj dodajRavnatelja(@RequestBody Ravnatelj ravnatelj) {
        return RavnateljService.dodajRavnatelj(ravnatelj);
    }

    @GetMapping("/ucionice")
    public List<Ucionica> listAllUcionice() {
        return ucionicaService.findAllUcionice();

    }

    @PostMapping("/ucionice/add")
    public Ucionica addUcionica(@RequestBody Ucionica ucionica) {
        return ucionicaService.dodajUcionica(ucionica);
    }

    @DeleteMapping("/ucionice/delete/{oznakaUc}")
    public ResponseEntity<String> deleteUcenik(@PathVariable String oznakaUc) {
        ucionicaService.deleteUcionica(oznakaUc);
        return ResponseEntity.ok("Uspjesno obrisana ucionica");
    }

    @PutMapping("/ucionica/promijeniKapacitet")
    public ResponseEntity<String> promijeniKapacitet(@PathVariable String oznakaUc, @RequestParam Integer noviKapacitet) {
        if (ucionicaService.findById(oznakaUc).isPresent()) {
            ucionicaService.findById(oznakaUc).get().setKapacitet(noviKapacitet);
            return ResponseEntity.ok("Učionici uspješno promijenjen kapacitet");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Došlo je do problema prilikom mijenjanja kapaciteta učionice");

        }
    }

    @GetMapping("/pogledajIzdanePotvrde")
    public List<ZahtjeviDTO> pregledIzdanihPotvrda() throws ParseException {

        List<ZahtjeviDTO> listaZahtjeva = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/db/zahtjevi.csv")))) {


            String line = reader.readLine();
            if (line == null) return listaZahtjeva;

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length == 3) {
                    listaZahtjeva.add(new ZahtjeviDTO(
                            row[0],
                            row[1],
                            dateFormat.parse(row[2].trim())
                    ));
                } else {
                    System.err.println("Invalid row: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        return listaZahtjeva;
    }

    @GetMapping("/pogledajIzdanePotvrdeImePrezime")
    public List<ZahtjeviDTO> pregledIzdanihPotvrdaImePrezime(@RequestParam String imeUcenik, @RequestParam String prezimeUcenik) throws ParseException {

        List<ZahtjeviDTO> listaZahtjeva = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/db/zahtjevi.csv")))) {


            String line = reader.readLine();
            if (line == null) return listaZahtjeva;

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length == 3 && row[0].equals(imeUcenik) && row[1].equals(prezimeUcenik)) {
                    listaZahtjeva.add(new ZahtjeviDTO(
                            row[0],
                            row[1],
                            dateFormat.parse(row[2].trim())
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        return listaZahtjeva;
    }

}

