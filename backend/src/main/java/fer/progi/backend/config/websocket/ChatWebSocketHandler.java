package fer.progi.backend.config.websocket;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;

import fer.progi.backend.domain.ChatMessage;
import fer.progi.backend.service.impl.ChatServiceJpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new ArrayList<>();
    private final Map<String, List<ChatMessage>> userMessages = new ConcurrentHashMap<>();
    private final Map<String, List<ChatMessage>> groupMessages = new ConcurrentHashMap<>();

    @Autowired
    private ChatServiceJpa chatService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    	
    	String email = session.getUri().getQuery().split("=")[1];
        session.getAttributes().put("email", email); // Spremajte email u atribute sesije
        sessions.add(session); // Dodajte sesiju u aktivne veze
        System.out.println("Nova veza s korisnikom: " + email);
        for(WebSocketSession s : sessions) {
        	System.out.println(s.getAttributes());
        }
        
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Pretvori tekstualnu poruku u objekt ChatMessage
        String payload = message.getPayload();
        System.out.println("Ovo je payload:");
        System.out.println(payload);
        ChatMessage chatMessage = parseMessage(payload); // Metoda za parsiranje poruke u ChatMessage
        
        System.out.println(chatMessage);
       

        // Provjeri je li poruka namijenjena grupi ili pojedincu
        if (chatMessage.getImeGrupe() != null) {
        	groupMessages.computeIfAbsent(chatMessage.getImeGrupe(), k -> new ArrayList<>()).add(chatMessage);
            chatService.saveMessage(chatMessage);
            sendToGrupa(chatMessage); // Pošalji poruku grupi
        } else {
            userMessages.computeIfAbsent(chatMessage.getPrimatelj(), k -> new ArrayList<>()).add(chatMessage);//pohrani poruku u memoriju
            chatService.saveMessage(chatMessage);//spremi poruku u bazu
            sendToPrimatelj(chatMessage); // Pošalji poruku pojedincu
        }
    }

    private void sendToGrupa(ChatMessage chatMessage) throws JsonProcessingException {
    	String jsonMessage = objectMapper.writeValueAsString(chatMessage);
        TextMessage textMessage = new TextMessage(jsonMessage);
        List<String> members = chatService.getMembersEmails(chatMessage.getImeGrupe()); // Dohvati članove grupe

        for (WebSocketSession session : sessions) {
            try {
                String sessionUserEmail = session.getAttributes().get("email").toString();
                if (members.contains(sessionUserEmail) && !chatMessage.getPosiljatelj().equals(sessionUserEmail)) {
                    session.sendMessage(textMessage);
                    System.out.println("Poruka poslana članu grupe: " + sessionUserEmail);
                }
            } catch (IOException e) {
                System.err.println("Greška prilikom slanja poruke članu grupe: " + e.getMessage());
            }
        }
		
	}

	@Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("Veza s klijentom prekinuta: " + session.getId());
    }

    // Slanje poruke primatelju
    public void sendToPrimatelj(ChatMessage chatMessage) throws IOException {
    	String jsonMessage = objectMapper.writeValueAsString(chatMessage);
        TextMessage textMessage = new TextMessage(jsonMessage);
        for (WebSocketSession session : sessions) {
            try {
                String sessionUserEmail = session.getAttributes().get("email").toString();

                if (sessionUserEmail.equals(chatMessage.getPrimatelj())) {
                    session.sendMessage(textMessage);
                    System.out.println("Poruka poslana primatelju: " + sessionUserEmail);
                } else {
                	System.out.println("To nije primatelj");
                }
            } catch (IOException e) {
                System.err.println("Greška prilikom slanja poruke: " + e.getMessage());
            }
        }
    }


 // Metoda za parsiranje JSON poruke u ChatMessage objekt
    private ChatMessage parseMessage(String payload) {
        ChatMessage chatMessage = new ChatMessage();
        
        // Ukloni početne i završne zagrade {}
        payload = payload.trim().substring(1, payload.length() - 1);

        // Razdvoji ključ:vrijednost parove
        String[] pairs = payload.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); // Regex za točno razdvajanje parova

        String primatelj = null;

        for (String pair : pairs) {
            // Razdvoji ključ i vrijednost po dvotočki
            String[] keyValue = pair.split(":", 2);
            String key = keyValue[0].trim().replace("\"", ""); // Ukloni navodnike
            String value = keyValue[1].trim().replace("\"", ""); // Ukloni navodnike

            // Postavi vrijednosti u ChatMessage
            switch (key) {
                case "posiljatelj":
                    chatMessage.setPosiljatelj(value);
                    break;
                case "primatelj":
                    primatelj = value;
                    break;
                case "sadrzaj":
                    chatMessage.setSadrzaj(value);
                    break;
                case "oznakaVremena":
                    // Parsiranje timestampa
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    chatMessage.setOznakaVremena(LocalDateTime.parse(value, formatter).atOffset(ZoneOffset.UTC).toLocalDateTime());
                    break;
            }
        }

        if (primatelj.contains("@")) {
        	chatMessage.setPrimatelj(primatelj);
        	chatMessage.setImeGrupe("null");
        } else {
        	chatMessage.setImeGrupe(primatelj);
        	chatMessage.setPrimatelj("null");
        	
        }

        return chatMessage;
    }


}