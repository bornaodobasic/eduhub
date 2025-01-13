package fer.progi.backend.config.websocket;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
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

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new ArrayList<>();

    @Autowired
    private ChatServiceJpa chatService;
    
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("Nova veza s klijentom: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Pretvori tekstualnu poruku u objekt ChatMessage
        String payload = message.getPayload();
        System.out.println("Ovo je payload:");
        System.out.println(payload);
        ChatMessage chatMessage = parseMessage(payload); // Metoda za parsiranje poruke u ChatMessage
        
        System.out.println(chatMessage);

        // Pohrani poruku u bazu podataka
        chatService.saveMessage(chatMessage);

        // Šalje poruku primatelju
        sendToPrimatelj(chatMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("Veza s klijentom prekinuta: " + session.getId());
    }

    // Slanje poruke primatelju
    public void sendToPrimatelj(ChatMessage chatMessage) throws IOException {
        TextMessage textMessage = new TextMessage(chatMessage.getSadrzaj());
        for (WebSocketSession session : sessions) {
            try {
                String sessionUser = session.getPrincipal().getName();
                System.out.println("Sesija: " + sessionUser);

                if (sessionUser.equals(chatMessage.getPrimatelj())) {
                    session.sendMessage(textMessage);
                    System.out.println("Poruka poslana primatelju: " + sessionUser);
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
                    chatMessage.setPrimatelj(value);
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
        
        return chatMessage;
    }

}

