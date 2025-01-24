package fer.progi.backend.config.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	@Autowired
	private ChatWebSocketHandler chatWebSocketHandler;
	
    @Bean
    public WebSocketHandler webSocketHandler() {
        return chatWebSocketHandler; // Vaša implementacija handlera
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Registriramo WebSocket handler za "/chat" endpoint
        registry.addHandler(chatWebSocketHandler, "/chat2", "/chat")
        		.setAllowedOrigins("http://localhost:8080");
        		
    }
}