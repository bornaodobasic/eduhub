package fer.progi.backend.dao;

import fer.progi.backend.domain.Admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.Djelatnik;

public interface DjelatnikRepository extends JpaRepository<Djelatnik,Integer> {
	Optional<Djelatnik> findByEmail(String email);
	
}