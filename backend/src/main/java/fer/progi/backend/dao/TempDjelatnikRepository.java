package fer.progi.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.Djelatnik;
import fer.progi.backend.domain.TempDjelatnik;

public interface TempDjelatnikRepository extends JpaRepository<TempDjelatnik,String> {
    Djelatnik findByEmail(String email);
}