
package fer.progi.backend.rest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fer.progi.backend.domain.Djelatnik;
import fer.progi.backend.service.DjelatnikService;



@RestController
@RequestMapping("/api/djelatnik")
@PreAuthorize("hasAuthority('Djelatnik')")
public class DjelatnikController {
	
	@Autowired
	private DjelatnikService DjelatnikService;
	
	@GetMapping("")
	public List<Djelatnik> listDjelatnik() {
		return DjelatnikService.listAll();
	}
	
	@PostMapping("/add")
	@Secured("ROLE_administrator")
	public Djelatnik dodajDjelatnik(@RequestBody Djelatnik djelatnik) {
		return DjelatnikService.dodajDjelatnik(djelatnik);
	}
	
	 @GetMapping("/pogledajIzdanePotvrde")
	    public List<ZahtjeviDTO> pregledIzdanihPotvrda() throws ParseException {
	        String csvFilePath = "database/zahjtevi.csv";
	        List<ZahtjeviDTO> listaZahtjeva = new ArrayList<>();

	        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
	            String line;
	            boolean isFirstLine = true; 

	            while ((line = br.readLine()) != null) {
	                if (isFirstLine) {
	                    isFirstLine = false; 
	                    continue;
	                }

	                System.out.println(line);
	                String[] row = line.split(",");
	                if (row.length == 3) {
	                    System.out.println(row[2]);
	                    listaZahtjeva.add(new ZahtjeviDTO(
	                            row[0],
	                            row[1],
	                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(row[2]) 
	                    ));
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        return listaZahtjeva;
	    }
	   
	    
	    @GetMapping("/pogledajIzdanePotvrdeImePrezime")
	    public List<ZahtjeviDTO> pregledIzdanihPotvrdaImePrezime(@RequestParam String imeUcenik, @RequestParam String prezimeUcenik) throws ParseException {
	        String csvFilePath = "database/zahjtevi.csv";
	        List<ZahtjeviDTO> listaZahtjeva = new ArrayList<>();

	        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
	            String line;
	            boolean isFirstLine = true; 

	            while ((line = br.readLine()) != null) {
	                if (isFirstLine) {
	                    isFirstLine = false; 
	                    continue;
	                }

	                String[] row = line.split(",");
	                
	                if (row[0].equals(imeUcenik) && row[1].equals(prezimeUcenik)) {
	                	  if (row.length == 3) {
	                          listaZahtjeva.add(new ZahtjeviDTO(
	                                  row[0],
	                                  row[1],
	                                  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(row[2]) 
	                          ));
	                      }
						
					}
	                
	              
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        return listaZahtjeva;
	    }
	   

}
