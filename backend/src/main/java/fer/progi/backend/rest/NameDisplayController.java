package fer.progi.backend.rest;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class NameDisplayController {

    @GetMapping("/api/user")
    public Map<String, String> getUserInfo(Authentication authentication) {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String name = oidcUser.getAttribute("name");
        return Map.of("name", name);
    }
}