package fer.progi.backend.dao;

import fer.progi.backend.domain.Ucenik;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UcenikRepository extends JpaRepository<Ucenik, Integer> {
    
    Optional<Ucenik> findByEmail(String email);
    void deleteByEmail(String email);

    boolean existsByEmail(String email);
}
