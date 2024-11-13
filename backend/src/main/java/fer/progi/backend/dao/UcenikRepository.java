package fer.progi.backend.dao;

import fer.progi.backend.domain.Admin;
import fer.progi.backend.domain.Ucenik;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UcenikRepository extends JpaRepository<Ucenik, String> {
    int countByOib(String oib);
    Ucenik findByEmail(String email);
}
