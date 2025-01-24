package fer.progi.backend.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fer.progi.backend.domain.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>{
	

	    @Query("SELECT m FROM ChatMessage m WHERE " +
	           "(m.posiljatelj = :posiljatelj AND m.primatelj = :primatelj) " +
	           "OR (m.posiljatelj = :primatelj AND m.primatelj = :posiljatelj) " +
	           "ORDER BY m.oznakaVremena ASC")
	    List<ChatMessage> findMessagesBetweenUsers(@Param("posiljatelj") String posiljatelj, 
	                                               @Param("primatelj") String primatelj);


		List<ChatMessage> findByImeGrupe(String imeGrupe);

	    @Query("SELECT m FROM ChatMessage m WHERE " +
		           "(m.primatelj = imeGrupe)" +
		           "ORDER BY m.oznakaVremena ASC")
		List<ChatMessage> findByPrimatelj(String imeGrupe);

}
