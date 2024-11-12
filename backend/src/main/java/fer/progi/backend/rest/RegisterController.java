package fer.progi.backend.rest;


import fer.progi.backend.domain.Admin;
//import fer.progi.backend.domain.Aktivnost;
import fer.progi.backend.service.AdminService;
//import fer.progi.backend.service.AktivnostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private AdminService adminService;

    @GetMapping("")
    public List<Admin> listAdmin() {
        return adminService.listAll();
    }

    @PostMapping("/nastavnik")
    public ResponseEntity<?> sendToAdmin(@RequestBody RegisterNastavnikDTO registerNastavnikDTO) {
        boolean success = adminService.addNastavnikToTempDB(registerNastavnikDTO);

        if (success) {
            return ResponseEntity.ok("Zahtjev uspješno poslan adminu");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Zahtjev nije poslan!");
        }
    }
    
    @PostMapping("/ravnatelj")
    public ResponseEntity<?> sendToAdmin(@RequestBody RegisterRavnateljDTO registerRavnateljDTO) {
        boolean success = adminService.addRavnateljToTempDB(registerRavnateljDTO);

        if (success) {
            return ResponseEntity.ok("Zahtjev uspješno poslan adminu");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Zahtjev nije poslan!");
        }
    }
    
    @PostMapping("/satnicar")
    public ResponseEntity<?> sendToAdmin(@RequestBody RegisterSatnicarDTO registerSatnicarDTO) {
        boolean success = adminService.addSatnicarToTempDB(registerSatnicarDTO);

        if (success) {
            return ResponseEntity.ok("Zahtjev uspješno poslan adminu");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Zahtjev nije poslan!");
        }
    }
    
    @PostMapping("/ucenik")
    public ResponseEntity<?> sendToAdmin(@RequestBody RegisterUcenikDTO registerUcenikDTO) {
        boolean success = adminService.addUcenikToTempDB(registerUcenikDTO);

        if (success) {
            return ResponseEntity.ok("Zahtjev uspješno poslan adminu");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Zahtjev nije poslan!");
        }
    }
    
    @PostMapping("/djelatnik")
    public ResponseEntity<?> sendToAdmin(@RequestBody RegisterDjelatnikDTO registerDjelatnikDTO) {
        boolean success = adminService.addDjelatnikToTempDB(registerDjelatnikDTO);

        if (success) {
            return ResponseEntity.ok("Zahtjev uspješno poslan adminu");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Zahtjev nije poslan!");
        }
    }
    
    @PostMapping("/admin")
    public ResponseEntity<?> sendToAdmin(@RequestBody RegisterAdminDTO registerAdminDTO) {
        boolean success = adminService.addAdminToTempDB(registerAdminDTO);

        if (success) {
            return ResponseEntity.ok("Zahtjev uspješno poslan adminu");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Zahtjev nije poslan!");
        }
    }
    
    
    

}
