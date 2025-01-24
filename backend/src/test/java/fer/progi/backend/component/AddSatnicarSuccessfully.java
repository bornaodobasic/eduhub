package fer.progi.backend.component;

import fer.progi.backend.dao.SatnicarRepository;
import fer.progi.backend.domain.Satnicar;
import fer.progi.backend.dto.AddDTO;
import fer.progi.backend.service.impl.AdminServiceJpa;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddSatnicarSuccessfully {

    @Mock
    private SatnicarRepository satnicarRepository;

    @InjectMocks
    private AdminServiceJpa adminService;

    @Test
    public void succesfullyAddSatnicar() {

        Satnicar satnicar = new Satnicar();
        satnicar.setEmail("satnicar@eduxhub.onmicrosoft.com");
        satnicar.setImeSatnicar("ImeSatnicara");
        satnicar.setPrezimeSatnicar("PrezimeSatnicara");


        AddDTO addDTO = new AddDTO();
        addDTO.setEmail("satnicar@eduxhub.onmicrosoft.com");
        addDTO.setIme("ImeSatnicara");
        addDTO.setPrezime("PrezimeSatnicara");

        when(satnicarRepository.save(Mockito.any(Satnicar.class))).thenReturn(satnicar);
        Satnicar savedSatnicar = adminService.addSatnicar(addDTO);

        Assertions.assertThat(savedSatnicar).isNotNull();
        Assertions.assertThat(savedSatnicar.getEmail()).isEqualTo("satnicar@eduxhub.onmicrosoft.com");
        Assertions.assertThat(savedSatnicar.getImeSatnicar()).isEqualTo("ImeSatnicara");
        Assertions.assertThat(savedSatnicar.getPrezimeSatnicar()).isEqualTo("PrezimeSatnicara");

        verify(satnicarRepository, times(1)).save(Mockito.any(Satnicar.class));

    }


}
