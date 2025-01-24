package fer.progi.backend.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.Satnicar;

public interface SatnicarRepository extends JpaRepository<Satnicar,Integer>{
   Optional<Satnicar> findByEmail(String email);
   void deleteByEmail(String email);
   boolean existsByEmail(String satnicarEmail);
}
