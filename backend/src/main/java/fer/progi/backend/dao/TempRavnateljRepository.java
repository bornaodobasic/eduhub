package fer.progi.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.Ravnatelj;

public interface TempRavnateljRepository extends JpaRepository<Ravnatelj, String>{

}
