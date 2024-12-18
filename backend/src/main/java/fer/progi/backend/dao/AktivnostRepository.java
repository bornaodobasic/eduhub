package fer.progi.backend.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.Aktivnost;

public interface AktivnostRepository extends JpaRepository<Aktivnost,Integer>{

	List<Aktivnost> findByOznAktivnostIn(List<String> naziviAktivnosti);

}
