package fer.progi.backend.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class ChatGroup {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String imeGrupe;
	
	@ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CHAT_GROUP_CLANOVI", joinColumns = @JoinColumn(name = "CHAT_GROUP_ID"))
    @Column(name = "CLAN_EMAIL")
    private List<String> clanovi = new ArrayList<>(); // Lista korisničkih ID-ova

}
