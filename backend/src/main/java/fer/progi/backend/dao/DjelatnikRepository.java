package fer.progi.backend.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.Djelatnik;

public interface DjelatnikRepository extends JpaRepository<Djelatnik,Integer> {
	Optional<Djelatnik> findByEmail(String email);
	void deleteByEmail(String email);
	boolean existsByEmail(String email);
}