package fer.progi.backend.component;

import fer.progi.backend.dao.RazredRepository;
import fer.progi.backend.dao.UcenikRepository;
import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.rest.AdminAddUcenikDTO;
import fer.progi.backend.service.impl.AdminServiceJpa;
import fer.progi.backend.service.impl.RazredServiceJpa;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvalidClassForStudent {


    @Mock
    private UcenikRepository ucenikRepository;

    @Mock
    private RazredRepository razredRepository;

    @Mock
    private RazredServiceJpa razredService;


    @InjectMocks
    private AdminServiceJpa adminService;


    @Test
    public void testInvalidClassForStudent() {
        Ucenik ucenik = new Ucenik();
        ucenik.setEmail("ucenik@eduxhub.onmicrosoft.com");
        ucenik.setImeUcenik("Ime");
        ucenik.setPrezimeUcenik("Prezime");
        ucenik.setOib("12345678912");
        ucenik.setSpol("M");
        ucenik.setVjeronauk(true);
        ucenik.setDatumRodenja("2000-01-01");

        AdminAddUcenikDTO addDTO = new AdminAddUcenikDTO();
        addDTO.setEmail("ucenik@eduxhub.onmicrosoft.com");
        addDTO.setImeUcenik("Ime");
        addDTO.setPrezimeUcenik("Prezime");
        addDTO.setOib("12345678912");
        addDTO.setSpol("M");
        addDTO.setVjeronauk(true);
        addDTO.setDatumRodenja("2000-01-01");
        addDTO.setRazred("5a");

        when(razredService.findByNazivRazred("5a")).thenReturn(null);

        Assertions.assertThatThrownBy(() -> adminService.addUcenik(addDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Razred s ovim nazivom ne postoji.");

        verify(ucenikRepository, never()).save(Mockito.any(Ucenik.class));



    }


}
