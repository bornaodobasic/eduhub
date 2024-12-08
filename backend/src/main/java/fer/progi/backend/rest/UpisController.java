package fer.progi.backend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import fer.progi.backend.service.impl.UcenikServiceJpa;

import java.util.Collection;
import java.util.HashSet;

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
    public ResponseEntity<String> postUpisPage(Authentication authentication, UpisDTO upisDTO) {

        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String email = (String) oidcUser.getAttributes().get("preferred_username");


        boolean isUpdated = ucenikServiceJpa.createNewUcenik(email, upisDTO);

        if (isUpdated) {
            Collection<GrantedAuthority> updatedAuthorities = new HashSet<>(authentication.getAuthorities());
            updatedAuthorities.removeIf(auth -> auth.getAuthority().equals("Upis"));
            updatedAuthorities.add(new SimpleGrantedAuthority("Ucenik"));

            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    authentication.getPrincipal(),
                    authentication.getCredentials(),
                    updatedAuthorities
            );
            SecurityContextHolder.getContext().setAuthentication(newAuth);

            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "/ucenik")
                    .build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update failed");
        }
    }

}
