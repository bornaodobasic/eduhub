package fer.progi.backend.dao;

import fer.progi.backend.domain.Admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.Ravnatelj;

public interface RavnateljRepository extends JpaRepository<Ravnatelj,Integer>{
   Optional<Ravnatelj> findByEmail(String email);
   void deleteByEmail(String email);
}
