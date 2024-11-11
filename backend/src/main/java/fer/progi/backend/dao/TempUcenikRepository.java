package fer.progi.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.TempUcenik;
import fer.progi.backend.domain.Ucenik;

public interface TempUcenikRepository extends JpaRepository<TempUcenik, String> {
    int countByOib(String oib);
    Ucenik findByEmail(String email);
}
