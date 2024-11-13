package fer.progi.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.Ravnatelj;
import fer.progi.backend.domain.TempRavnatelj;

public interface TempRavnateljRepository extends JpaRepository<TempRavnatelj, String>{

}
