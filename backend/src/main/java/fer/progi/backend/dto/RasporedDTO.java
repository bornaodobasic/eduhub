package fer.progi.backend.dto;

import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class RasporedDTO {
    private DayOfWeek dan;
    private LocalTime pocetakSata;
    private LocalTime krajSata;
    private String predmet;
    private String nastavnik;
    private String ucionica;
}
