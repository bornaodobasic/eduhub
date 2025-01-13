package fer.progi.backend.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ChatMessage {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String posiljatelj;
    private String primatelj; // null za grupne poruke
    private String imeGrupe;   // null za privatne poruke
    private String sadrzaj;

    private LocalDateTime oznakaVremena = LocalDateTime.now();
    
    
}
