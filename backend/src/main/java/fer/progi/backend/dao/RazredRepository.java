package fer.progi.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.Razred;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RazredRepository extends JpaRepository<Razred, String>{

    Optional<Razred> findByNazRazred(String nazRazred);

    @Query("SELECT r FROM Razred r WHERE r.smjer.nazivSmjer = :smjer")
    List<Razred> findAllBySmjer_NazivSmjer(@Param("smjer") String smjer);

}
