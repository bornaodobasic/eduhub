package fer.progi.backend.rest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fer.progi.backend.config.websocket.ChatWebSocketHandler;
import fer.progi.backend.domain.ChatGroup;
import fer.progi.backend.domain.ChatMessage;
import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.service.NastavnikService;
import fer.progi.backend.service.UcenikService;
import fer.progi.backend.service.impl.ChatServiceJpa;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    private NastavnikService nastavnikService;
    
    @Autowired
    private UcenikService ucenikService;

    @Autowired
    private ChatWebSocketHandler chatWebSocketHandler;  // Povezivanje sa WebSocket handlerom
    
    @Autowired
    private ChatServiceJpa chatService;

    // Endpoint za slanje poruke, koja ide prema WebSocket handleru
    @PostMapping("/sendMessage")
    public void sendMessage(@RequestBody ChatMessage message) throws IOException {
        chatWebSocketHandler.sendToPrimatelj(message);
    }
    
    @GetMapping("/getUserName/{email}")
    public String getUserName(@PathVariable String email) {
    	System.out.println("Dohvaćen email za ime: " + email); 
    	Optional<Ucenik> u = ucenikService.findByEmailUcenik(email);
        if (u.isPresent()) {
            return u.get().getImeUcenik() + " " + u.get().getPrezimeUcenik();
        }
        
        Optional<Nastavnik> n = nastavnikService.findByEmail(email);
        if (n.isPresent()) {
            return n.get().getImeNastavnik() + " " + n.get().getPrezimeNastavnik();
        }
        
        return email;
    }
    
    @GetMapping("/messages")
    public ResponseEntity<List<ChatMessage>> getMessagesBetweenUsers(
            @RequestParam(name = "korisnik1") String posiljatelj,
            @RequestParam(name = "korisnik2") String primatelj) {

		// Dohvaćanje poruka između dva korisnika
        List<ChatMessage> messages = chatService.getMessagesBetweenUsers(posiljatelj, primatelj);
        for(ChatMessage m : messages) {
        	System.out.println(m);
        }

        return ResponseEntity.ok(messages); // Vraća listu poruka kao JSON
    }
    
	@GetMapping("/email")
	public String getUserEmailString(Authentication authentication) {
		OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
		String email = oidcUser.getAttribute("preferred_username");
		return email;
	}
	
	@PostMapping("/kreirajGrupu")
	public void kreirajGrupu(@RequestBody ChatGroup chatGroup, Authentication authentication) {
		List<String> clanovi = chatGroup.getClanovi();
		OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
		String email = oidcUser.getAttribute("preferred_username");
		clanovi.add(email);
		chatService.kreirajGrupu(clanovi, chatGroup.getImeGrupe());
	
	}
	
	@GetMapping("/grupe")
	public List<String> listaGrupa(Authentication authentication) {
		OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
		String email = oidcUser.getAttribute("preferred_username");
		return chatService.getImenaGrupa(email);
	}
	
	@GetMapping("/groupMessages/{imeGrupe}")
	public ResponseEntity<List<ChatMessage>> getMessagesForGroup(
	        @PathVariable String imeGrupe) {
	    
	    // Dohvati poruke za grupu koristeći servis
	    List<ChatMessage> groupMessages = chatService.getMessagesForGroup(imeGrupe);

	    return ResponseEntity.ok(groupMessages); // Ako ima poruka, vraća ih kao JSON
	}

	
    
    
}