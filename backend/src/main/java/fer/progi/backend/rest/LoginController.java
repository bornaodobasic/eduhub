package fer.progi.backend.rest;

import org.springframework.web.bind.annotation.CrossOrigin;

import fer.progi.backend.domain.Admin;
import fer.progi.backend.domain.Djelatnik;
import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.domain.Ravnatelj;
import fer.progi.backend.domain.Satnicar;
import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.service.AdminService;
import fer.progi.backend.service.DjelatnikService;
import fer.progi.backend.service.NastavnikService;
import fer.progi.backend.service.RavnateljService;
import fer.progi.backend.service.SatnicarService;
import fer.progi.backend.service.UcenikService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/loginUser")
public class LoginController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private NastavnikService nastavnikService;
    
    @Autowired 
    private UcenikService ucenikService;
    
    @Autowired 
    private AdminService adminService;
    
    @Autowired
    private DjelatnikService djelatnikService;
    
    @Autowired 
    private RavnateljService ravnateljService;
    
    @Autowired 
    private SatnicarService satnicarService;
    
    @PostMapping("")
    public ResponseEntity<?> prebaciKorisnika(@RequestBody LoginDTO loginDTO) {
        Optional<Nastavnik> nastavnikMaybe = nastavnikService.pronadiNastavnikaPoEmail(loginDTO.getEmail());
        Optional<Ucenik> ucenikMaybe = ucenikService.pronadiUcenikaPoEmail(loginDTO.getEmail());
        Optional<Djelatnik> djelatnikMaybe = djelatnikService.pronadiDjelatnikaPoEmail(loginDTO.getEmail());
        Optional<Ravnatelj> ravnateljMaybe = ravnateljService.pronadiRavnateljaPoEmail(loginDTO.getEmail());
        Optional<Satnicar> satnicarMaybe = satnicarService.pronadiSatnicaraPoEmail(loginDTO.getEmail());

        if (nastavnikMaybe.isPresent()) {
            Nastavnik nastavnik = nastavnikMaybe.get();

            if (!passwordEncoder.matches(loginDTO.getLozinka(), nastavnik.getLozinka())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Neispravna lozinka");
            }

            UserDetails userDetails = new User(
              nastavnik.getEmail(),
              nastavnik.getLozinka(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_nastavnik")
            );

            return ResponseEntity.ok("Prijavljen kao nastavnik");
        }else if(ucenikMaybe.isPresent()) {
        	Ucenik ucenik = ucenikMaybe.get();

            if (!passwordEncoder.matches(loginDTO.getLozinka(), ucenik.getLozinka())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Neispravna lozinka");
            }

            UserDetails userDetails = new User(
              ucenik.getEmail(),
              ucenik.getLozinka(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ucenik")
            );

            return ResponseEntity.ok("Prijavljen kao učenik");
        	
        }else if(djelatnikMaybe.isPresent()) {
        	Djelatnik djelatnik = djelatnikMaybe.get();

            if (!passwordEncoder.matches(loginDTO.getLozinka(), djelatnik.getLozinka())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Neispravna lozinka");
            }

            UserDetails userDetails = new User(
              djelatnik.getEmail(),
              djelatnik.getLozinka(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_djelatnik")
            );

            return ResponseEntity.ok("Prijavljen kao djelatnik");
        
        }else if (ravnateljMaybe.isPresent()) {
            Ravnatelj ravnatelj = ravnateljMaybe.get();

            if (!passwordEncoder.matches(loginDTO.getLozinka(), ravnatelj.getLozinka())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Neispravna lozinka");
            }

            UserDetails userDetails = new User(
              ravnatelj.getEmail(),
              ravnatelj.getLozinka(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ravnatelj")
            );

            return ResponseEntity.ok("Prijavljen kao ravnatelj");
        }else if (satnicarMaybe.isPresent()) {
            Satnicar satnicar = satnicarMaybe.get();

            if (!passwordEncoder.matches(loginDTO.getLozinka(), satnicar.getLozinka())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Neispravna lozinka");
            }

            UserDetails userDetails = new User(
              satnicar.getEmail(),
              satnicar.getLozinka(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_satnicar")
            );

            return ResponseEntity.ok("Prijavljen kao satnicar");
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Korisnik nije pronađen.");
        }

    }
    
    /*    
    //Ucenik login-----------------------------------------------------------------------------------
    
    @PostMapping("/ucenik")
    public ResponseEntity<?> prebaciNaUcenika(@RequestBody LoginDTO loginDTO) {
        Optional<Ucenik> ucenikMaybe = ucenikService.pronadiUcenikaPoEmail(loginDTO.getEmail());

        if (ucenikMaybe.isPresent()) {
            Ucenik ucenik = ucenikMaybe.get();

            if (!passwordEncoder.matches(loginDTO.getLozinka(), ucenik.getLozinka())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Neispravna lozinka");
            }

            UserDetails userDetails = new User(
              ucenik.getEmail(),
              ucenik.getLozinka(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ucenik")
            );

            return ResponseEntity.ok("Prijavljen kao učenik");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Učenik nije pronađen.");
        }

    }
    
    //Djelatnik login--------------------------------------------------------------------------------
    
    @PostMapping("/djelatnik")
    public ResponseEntity<?> prebaciNaDjelatnika(@RequestBody LoginDTO loginDTO) {
        Optional<Djelatnik> djelatnikMaybe = djelatnikService.pronadiDjelatnikaPoEmail(loginDTO.getEmail());

        if (djelatnikMaybe.isPresent()) {
            Djelatnik djelatnik = djelatnikMaybe.get();

            if (!passwordEncoder.matches(loginDTO.getLozinka(), djelatnik.getLozinka())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Neispravna lozinka");
            }

            UserDetails userDetails = new User(
              djelatnik.getEmail(),
              djelatnik.getLozinka(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_djelatnik")
            );

            return ResponseEntity.ok("Prijavljen kao djelatnik");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Djelatnik nije pronađen.");
        }

    }
    
    //Admin login-----------------------------------------------------------------------------------
    
    @PostMapping("/admin")
    public ResponseEntity<?> prebaciNaAdmina(@RequestBody LoginDTO loginDTO) {
        Optional<Admin> adminMaybe = adminService.pronadiAdminaPoEmail(loginDTO.getEmail());

        if (adminMaybe.isPresent()) {
            Admin admin = adminMaybe.get();

            if (!passwordEncoder.matches(loginDTO.getLozinka(), admin.getLozinka())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Neispravna lozinka");
            }

            UserDetails userDetails = new User(
              admin.getEmail(),
              admin.getLozinka(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_admin")
            );

            return ResponseEntity.ok("Prijavljen kao admin");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin nije pronađen.");
        }

    }
    
    //Ravnatelj login-------------------------------------------------------------------------------------
    
    @PostMapping("/ravnatelj")
    public ResponseEntity<?> prebaciNaRavnatelja(@RequestBody LoginDTO loginDTO) {
        Optional<Ravnatelj> ravnateljMaybe = ravnateljService.pronadiRavnateljaPoEmail(loginDTO.getEmail());

        if (ravnateljMaybe.isPresent()) {
            Ravnatelj ravnatelj = ravnateljMaybe.get();

            if (!passwordEncoder.matches(loginDTO.getLozinka(), ravnatelj.getLozinka())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Neispravna lozinka");
            }

            UserDetails userDetails = new User(
              ravnatelj.getEmail(),
              ravnatelj.getLozinka(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ravnatelj")
            );

            return ResponseEntity.ok("Prijavljen kao ravnatelj");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ravnatelj nije pronađen.");
        }

    }
    
    //Satnicar login---------------------------------------------------------------------------------------
    
    @PostMapping("/satnicar")
    public ResponseEntity<?> prebaciNaSatnicara(@RequestBody LoginDTO loginDTO) {
        Optional<Satnicar> satnicarMaybe = satnicarService.pronadiSatnicaraPoEmail(loginDTO.getEmail());

        if (satnicarMaybe.isPresent()) {
            Satnicar satnicar = satnicarMaybe.get();

            if (!passwordEncoder.matches(loginDTO.getLozinka(), satnicar.getLozinka())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Neispravna lozinka");
            }

            UserDetails userDetails = new User(
              satnicar.getEmail(),
              satnicar.getLozinka(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_satnicar")
            );

            return ResponseEntity.ok("Prijavljen kao satnicar");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Satnicar nije pronađen.");
        }

    }*/




}
