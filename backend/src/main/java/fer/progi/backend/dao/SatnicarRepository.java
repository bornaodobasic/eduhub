package fer.progi.backend.dao;

import fer.progi.backend.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.Satnicar;

public interface SatnicarRepository extends JpaRepository<Satnicar,Integer>{
    Satnicar findByEmail(String email);
}
