package fer.progi.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.ChatGroup;

public interface ChatGroupRepository extends JpaRepository<ChatGroup, Long>{
	ChatGroup findByImeGrupe(String imeGrupe);

}
