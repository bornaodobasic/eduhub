package fer.progi.backend.dao;

import fer.progi.backend.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.Ravnatelj;

public interface RavnateljRepository extends JpaRepository<Ravnatelj,Integer>{
    Ravnatelj findByEmail(String email);
}
