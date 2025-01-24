package fer.progi.backend.component;

import fer.progi.backend.dao.AktivnostRepository;
import fer.progi.backend.dao.UcenikRepository;
import fer.progi.backend.domain.Aktivnost;
import fer.progi.backend.domain.Razred;
import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.service.impl.AktivnostServiceJpa;
import fer.progi.backend.service.impl.RazredServiceJpa;
import fer.progi.backend.service.impl.UcenikServiceJpa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddActivitySuccessfully {

    @Mock
    private UcenikRepository ucenikRepository;

    @Mock
    private AktivnostRepository aktivnostRepository;

    @Mock
    private RazredServiceJpa razredService;

    @Mock
    private AktivnostServiceJpa aktivnostService;


    @InjectMocks
    private UcenikServiceJpa ucenikService;



    @Test
    public void testAddActivitySuccessfully() {

        Razred mockRazred = new Razred();
        mockRazred.setNazRazred("1a");

        Ucenik ucenik = new Ucenik();
        ucenik.setImeUcenik("Nikola");
        ucenik.setPrezimeUcenik("Nikić");
        ucenik.setOib("34145676191");
        ucenik.setDatumRodenja("2009-10-29");
        ucenik.setRazred(mockRazred);
        ucenik.setSpol("M");
        ucenik.setEmail("nikola.nikic@eduxhub.onmicrosoft.com");
        ucenik.setVjeronauk(true);

        List<Aktivnost> aktivnosti = aktivnostService.findByOznAktivnosti(List.of("Nogomet", "Dodatna_nastava_Matematika", "Dodatna_nastava_Engleski_jezik"));
        ucenik.setAktivnosti(aktivnosti);

        Aktivnost mockAktivnost = new Aktivnost();
        mockAktivnost.setOznAktivnost("Šah");
        List<Aktivnost> newAktivnosti = Arrays.asList(mockAktivnost);
        when(aktivnostService.findByOznAktivnosti(List.of("Šah"))).thenReturn(newAktivnosti);

        Aktivnost akt = newAktivnosti.getFirst();

        when(ucenikRepository.findByEmail("nikola.nikic@eduxhub.onmicrosoft.com")).thenReturn(Optional.of(ucenik));
        when(ucenikRepository.save(Mockito.any(Ucenik.class))).thenReturn(ucenik);

        ucenikService.dodajAktivnostiPoNazivu(ucenik.getEmail(), List.of("Šah"));

        assertThat(ucenik.getAktivnosti()).contains(akt);

        verify(ucenikRepository, times(1)).save(Mockito.any(Ucenik.class));
    }

}
