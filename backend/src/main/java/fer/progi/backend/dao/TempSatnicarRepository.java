package fer.progi.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fer.progi.backend.domain.Satnicar;

public interface TempSatnicarRepository extends JpaRepository<Satnicar, String> {

}
