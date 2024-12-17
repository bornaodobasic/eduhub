package fer.progi.backend.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.Nastavnik;

public interface NastavnikRepository extends JpaRepository<Nastavnik, Integer>{
    Optional<Nastavnik> findByEmail(String email);
    void deleteByEmail(String email);
}
