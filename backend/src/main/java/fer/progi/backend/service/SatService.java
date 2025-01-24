package fer.progi.backend.service;

import fer.progi.backend.domain.Sat;
import java.time.DayOfWeek;
import java.util.List;

public interface SatService {

    void generirajRaspored();
    DayOfWeek findDay(int sat);
    List<Sat> listAll();
    void dodijeliRazrednike();
	
	
}
