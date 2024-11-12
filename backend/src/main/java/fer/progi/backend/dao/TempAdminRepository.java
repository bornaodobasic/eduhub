package fer.progi.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

//import fer.progi.backend.domain.Admin;
import fer.progi.backend.domain.TempAdmin;

public interface TempAdminRepository extends JpaRepository<TempAdmin,String> {
	    //Admin findByEmail(String email);
}
