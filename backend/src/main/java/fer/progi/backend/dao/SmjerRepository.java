package fer.progi.backend.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.Smjer;

public interface SmjerRepository extends JpaRepository<Smjer, Integer>{
	
	Optional<Smjer> findByNazivSmjer(String naziv);

}
