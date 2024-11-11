package fer.progi.backend.dao;

import fer.progi.backend.domain.TempNastavnik;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TempNastavnikRepository extends JpaRepository<TempNastavnik, String> {

}
