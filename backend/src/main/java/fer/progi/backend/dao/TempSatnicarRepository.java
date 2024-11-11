package fer.progi.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.Satnicar;
import fer.progi.backend.domain.TempSatnicar;

public interface TempSatnicarRepository extends JpaRepository<TempSatnicar, String> {

}
