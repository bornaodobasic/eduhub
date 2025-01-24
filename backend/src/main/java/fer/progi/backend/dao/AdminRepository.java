package fer.progi.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.Admin;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Integer>{
    Optional<Admin> findByEmail(String email);
    
    void deleteByEmail(String email);
}
