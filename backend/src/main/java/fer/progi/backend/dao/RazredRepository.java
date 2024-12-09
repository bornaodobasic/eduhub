package fer.progi.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.Razred;

import java.util.List;
import java.util.Optional;

public interface RazredRepository extends JpaRepository<Razred, String>{

    Optional<Razred> findByNazRazred(String nazRazred);
    
    int countByNazRazred(String nazRazred);

    List<Razred> findAllBySmjer_NazivSmjer(String smjer);
}
