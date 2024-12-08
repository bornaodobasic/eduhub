package fer.progi.backend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import fer.progi.backend.service.impl.UcenikServiceJpa;

@Controller
@RequestMapping("/upis")
public class UpisController {
	
    @Autowired
    private UcenikServiceJpa ucenikServiceJpa;

    @GetMapping
    public String redirectToXyHtml(Model model) {
        return "upis";
    }


    @PostMapping("")
    public ResponseEntity<String> postUpisPage(Authentication authentication, RegisterUcenikDTO registerUcenikDTO) {

        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String email = (String) oidcUser.getAttributes().get("preferred_username");

       
        boolean isUpdated = ucenikServiceJpa.createNewUcenik(
                email,
                registerUcenikDTO.getImeUcenik(),
                registerUcenikDTO.getPrezimeUcenik(),
                registerUcenikDTO.getSpol()
        );

        if (isUpdated) {
            return ResponseEntity.ok("Update successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update failed");
        }
    }

}
