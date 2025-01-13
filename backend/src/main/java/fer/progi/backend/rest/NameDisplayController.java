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

	@GetMapping("/api/user/email")
	public Map<String, String> getUserEmail(Authentication authentication) {
		OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
		String email = oidcUser.getAttribute("preferred_username");
		return Map.of("email", email);
	}

	@GetMapping("/api/user/emailOnly")
	public String getUserEmailString(Authentication authentication) {
	    if (authentication == null || authentication.getPrincipal() == null) {
	        return "Nema prijavljenog korisnika";  // Vratite neki default odgovor ili grešku
	    }
	    OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
	    String email = oidcUser.getAttribute("preferred_username");
	    return email;
	}
}
