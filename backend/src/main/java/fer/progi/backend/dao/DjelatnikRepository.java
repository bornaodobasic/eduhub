package fer.progi.backend.dao;

import fer.progi.backend.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.Djelatnik;

public interface DjelatnikRepository extends JpaRepository<Djelatnik,Integer> {
    Djelatnik findByEmail(String email);
}