package fer.progi.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.Nastavnik;

public interface NastavnikRepository extends JpaRepository<Nastavnik, Integer>{
    Nastavnik findByEmail(String email);
}
