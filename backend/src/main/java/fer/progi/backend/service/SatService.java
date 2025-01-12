package fer.progi.backend.service;

import java.time.DayOfWeek;

public interface SatService {

    void generirajRaspored();
    DayOfWeek findDay(int sat);
}
