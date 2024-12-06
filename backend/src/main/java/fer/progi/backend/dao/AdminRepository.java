package fer.progi.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.Admin;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin,Integer>{
    Optional<Admin> findByEmail(String email);
}
