package fer.progi.backend.rest;


import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.service.NastavnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private NastavnikService nastavnikService;

    @PostMapping("/nastavnik")
    @Secured("ROLE_GUEST")
    public ResponseEntity<?> prebaciNaNastavnika(@RequestBody LoginDTO loginDTO) {
        Optional<Nastavnik> nastavnikMaybe = nastavnikService.pronadiNastavnikaPoEmail(loginDTO.getEmail());

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
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nastavnik nije pronađen.");
        }

    }


}
