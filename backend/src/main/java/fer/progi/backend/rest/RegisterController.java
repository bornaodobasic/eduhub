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
    
    boolean success = false;

    @PostMapping("")
    public ResponseEntity<?> sendToAdmin(@RequestBody RegisterKorisnikDTO registerKorisnikDTO) {
    	if(registerKorisnikDTO.getRole().toLowerCase().equals("nastavnik")) {
    		success = adminService.addNastavnikToTempDB(registerKorisnikDTO);
    	} else if(registerKorisnikDTO.getRole().toLowerCase().equals("admin")) {
    	   success = adminService.addAdminToTempDB(registerKorisnikDTO);

    	} else if(registerKorisnikDTO.getRole().toLowerCase().equals("ravnatelj")) {
     	   success = adminService.addRavnateljToTempDB(registerKorisnikDTO);

     	} else if(registerKorisnikDTO.getRole().toLowerCase().equals("satnicar")) {
     	   success = adminService.addSatnicarToTempDB(registerKorisnikDTO);

     	} else if(registerKorisnikDTO.getRole().toLowerCase().equals("djelatnik")) {
      	   success = adminService.addDjelatnikToTempDB(registerKorisnikDTO);

      	} else {
      		success = false;
      	}
        
    	if (success) {
            return ResponseEntity.ok("Zahtjev uspješno poslan adminu");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Zahtjev nije poslan!");
        }
    }
    

   /* public ResponseEntity<?> sendToAdmin(@RequestBody RegisterRavnateljDTO registerRavnateljDTO) {
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
    
    if (success) {
        return ResponseEntity.ok("Zahtjev uspješno poslan adminu");
    } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Zahtjev nije poslan!");
    }*/
    

}
