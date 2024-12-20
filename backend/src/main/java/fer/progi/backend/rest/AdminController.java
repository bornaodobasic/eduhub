package fer.progi.backend.rest;

import fer.progi.backend.domain.*;
import fer.progi.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('Admin')")
public class AdminController {

    @Autowired
    private AdminService AdminService;

    @Autowired
    private DjelatnikService DjelatnikService;

    @Autowired
    private NastavnikService NastavnikService;

    @Autowired
    private SatnicarService SatnicarService;

    @Autowired
    private RavnateljService RavnateljService;

    @Autowired
    private UcenikService UcenikService;

    @GetMapping("/admin")
    public List<Admin> listAllAdmins() {
        return AdminService.findAllAdmins();
    }

    @PostMapping("/admin/add")
    public Admin addAdmin(@RequestBody AddDTO addDTO) {
        return AdminService.addAdmin(addDTO);
    }

    @DeleteMapping("/admin/delete/{email}")
    public ResponseEntity<String> deleteAdmin(@PathVariable String email) {
        AdminService.deleteAdmin(email);
        return ResponseEntity.ok("Obrisan admin s emailom " + email + ".");
    }

    @GetMapping("/djelatnik")
    public List<Djelatnik> listAllDjelatniks() {
        return DjelatnikService.findAllDjelatniks();
    }

    @PostMapping("/djelatnik/add")
    public Djelatnik addDjelatnik(@RequestBody AddDTO addDTO) {
        return AdminService.addDjelatnik(addDTO);
    }

    @DeleteMapping("/djelatnik/delete/{email}")
    public ResponseEntity<String> deleteDjelatnik(@PathVariable String email) {
        DjelatnikService.deleteDjelatnik(email);
        return ResponseEntity.ok("Obrisan djelatnik s emailom " + email + ".");
    }

    @GetMapping("/satnicar")
    public List<Satnicar> listAllSatnicars() {
        return SatnicarService.findAllSatnicars();
    }

    @PostMapping("/satnicar/add")
    public Satnicar addSatnicar(@RequestBody AddDTO addDTO) {
        return AdminService.addSatnicar(addDTO);
    }

    @DeleteMapping("/satnicar/delete/{email}")
    public ResponseEntity<String> deleteSatnicar(@PathVariable String email) {
        SatnicarService.deleteSatnicar(email);
        return ResponseEntity.ok("Obrisan satnicar s emailom " + email + ".");
    }

    @GetMapping("/nastavnik")
    public List<Nastavnik> listAllNastavniks() {
        return NastavnikService.findAllNastavniks();
    }

    @PostMapping("/nastavnik/add")
    public Nastavnik addNastavnik(@RequestBody AddDTO addDTO) {
        return AdminService.addNastavnik(addDTO);
    }

    @DeleteMapping("/nastavnik/delete/{email}")
    public ResponseEntity<String> deleteNastavnik(@PathVariable String email) {
        NastavnikService.deleteNastavnik(email);
        return ResponseEntity.ok("Obrisan nastavnik s emailom " + email + ".");
    }

    @GetMapping("/ravnatelj")
    public List<Ravnatelj> listAllRavnateljs() {
        return RavnateljService.findAllRavnateljs();
    }

    @PostMapping("/ravnatelj/add")
    public Ravnatelj addRavnatelj(@RequestBody AddDTO addDTO) {
        return AdminService.addRavnatelj(addDTO);
    }

    @DeleteMapping("/ravnatelj/delete/{email}")
    public ResponseEntity<String> deleteRavnatelj(@PathVariable String email) {
        RavnateljService.deleteRavnatelj(email);
        return ResponseEntity.ok("Obrisan ravnatelj s emailom " + email + ".");
    }

    @GetMapping("/ucenik")
    public List<Ucenik> listAllUceniks() {
        return UcenikService.findAllUceniks();
    }

    @PostMapping("/ucenik/add")
    public Ucenik addUcenik(@RequestBody AdminAddUcenikDTO adminAddUcenikDTO) {
        return AdminService.addUcenik(adminAddUcenikDTO);
    }

    @DeleteMapping("/ucenik/delete/{email}")
    public ResponseEntity<String> deleteUcenik(@PathVariable String email) {
        UcenikService.deleteUcenik(email);
        return ResponseEntity.ok("Obrisan ucenik s emailom " + email + ".");
    }
    
    @GetMapping("/ucenik/aktivnosti/{email}")
    	public Set<Aktivnost> getUcenikAktivnosti(@PathVariable String email) {
    	    return UcenikService.findUcenikAktivnosti(email);
    	}
    
    @PostMapping("/ucenik/aktivnosti/add/{email}")
    public ResponseEntity<String> addAktivnostUcenik( @PathVariable String email, @RequestBody List<String> naziviAktivnosti) {
        
            boolean rezultat = UcenikService.dodajAktivnostiPoNazivu(email, naziviAktivnosti);
            
            if (rezultat) {
                return ResponseEntity.ok("Aktivnosti uspješno dodane učeniku");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Došlo je do problema prilikom dodavanja aktivnosti učeniku.");
            }
       
    }
    
    @DeleteMapping("/ucenik/aktivnosti/delete/{email}")
    public ResponseEntity<String> deleteAktivnostUcenik(@PathVariable String email,  @RequestBody List<String> naziviAktivnosti ){
    	 boolean rezultat = UcenikService.ukloniAktivnostiPoNazivu(email, naziviAktivnosti);
         
         if (rezultat) {
             return ResponseEntity.ok("Aktivnosti uspješno uklonjene učeniku");
         } else {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                     .body("Došlo je do problema prilikom uklanjanja aktivnosti učeniku.");
         }
    	
    }
    
    @GetMapping("/nastavnik/predmeti/{email}")
	public Set<Predmet> getPredmetNastavnik(@PathVariable String email) {
	    return NastavnikService.findNastavnikPredmeti(email);
	}
    
    @PostMapping("/nastavnik/predmeti/add/{email}")
    public ResponseEntity<String> addPredmetNastavnik( @PathVariable String email, @RequestBody List<String> predmeti) {
        
            boolean rezultat = NastavnikService.dodajPredmetNastavniku(email, predmeti);
            
            if (rezultat) {
                return ResponseEntity.ok("Predmet uspješno dodan nastavniku");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Došlo je do problema prilikom dodavanja predmeta nastavniku.");
            }
       
    }
    
    @DeleteMapping("/nastavnik/predmeti/delete/{email}")
    public ResponseEntity<String> deletePredmetNastavnik(@PathVariable String email,  @RequestBody List<String> predmeti ){
    	boolean rezultat = NastavnikService.ukloniPredmetNastavniku(email, predmeti);
         
         if (rezultat) {
             return ResponseEntity.ok("Predmeti uspješno uklonjeni nastavniku");
         } else {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                     .body("Došlo je do problema prilikom uklanjanja predmeta nastavniku.");
         }
    	
    }

}


