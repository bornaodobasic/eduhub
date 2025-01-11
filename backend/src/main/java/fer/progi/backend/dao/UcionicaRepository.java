package fer.progi.backend.dao;

import fer.progi.backend.domain.Ucionica;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UcionicaRepository extends JpaRepository<Ucionica, String> {
    boolean existsByOznakaUc(String oznaka);
}
