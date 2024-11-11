package fer.progi.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.Admin;

public interface AdminRepository extends JpaRepository<Admin,Integer>{

}
