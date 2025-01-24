package fer.progi.backend.component;

import fer.progi.backend.domain.Ucionica;
import fer.progi.backend.rest.RavnateljController;
import fer.progi.backend.service.impl.UcionicaServiceJpa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChangeClassroomCapacity {

    @Mock
    private UcionicaServiceJpa ucionicaService;

    @InjectMocks
    private RavnateljController ravnateljController;

    @Test
    public void changeClassroomCapacity() {

        String oznakaUc = "A202";
        Integer noviKapacitet = 50;
        Ucionica mockUcionica = new Ucionica();
        mockUcionica.setKapacitet(20);

        when(ucionicaService.findById(oznakaUc)).thenReturn(Optional.of(mockUcionica))
                .thenReturn(Optional.of(mockUcionica));

        ResponseEntity<String> response = ravnateljController.promijeniKapacitet(oznakaUc, noviKapacitet);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Učionici uspješno promijenjen kapacitet");

        verify(ucionicaService, times(2)).findById(oznakaUc);
    }

}
