package fer.progi.backend.dao;

import fer.progi.backend.domain.VrijemeSata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.time.LocalTime;

public interface VrijemeSataRepository  extends JpaRepository<VrijemeSata, Integer> {

    boolean existsByDanAndPocetakSataAndKrajSata(DayOfWeek dan, LocalTime pocetakSata, LocalTime krajSata);

}
