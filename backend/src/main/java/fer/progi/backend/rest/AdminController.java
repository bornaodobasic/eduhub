package fer.progi.backend.rest;

import fer.progi.backend.domain.*;
import fer.progi.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

}


