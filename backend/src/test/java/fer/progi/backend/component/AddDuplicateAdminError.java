package fer.progi.backend.component;

import fer.progi.backend.dao.AdminRepository;
import fer.progi.backend.domain.Admin;
import fer.progi.backend.rest.AddDTO;
import fer.progi.backend.service.impl.AdminServiceJpa;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddDuplicateAdminError {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminServiceJpa adminService;

    @Test
    public void shouldThrowExceptionWhenAdminWithEmailExists() {

        Admin existing = new Admin();
        existing.setPrezimeAdmin("Jelavić");
        existing.setImeAdmin("Josip");
        existing.setEmail("josip.jelavic@eduxhub.onmicrosoft.com");

        Admin admin = new Admin();
        admin.setPrezimeAdmin("NovoPrezime");
        admin.setImeAdmin("NovoIme");
        admin.setEmail("josip.jelavic@eduxhub.onmicrosoft.com");

        AddDTO addDTO = new AddDTO();
        addDTO.setPrezime("NovoPrezime");
        addDTO.setIme("NovoIme");
        addDTO.setEmail("josip.jelavic@eduxhub.onmicrosoft.com");

        when(adminRepository.findByEmail("josip.jelavic@eduxhub.onmicrosoft.com")).thenReturn(Optional.of(existing));

        Assertions.assertThatThrownBy(() -> adminService.addAdmin(addDTO))
                        .isInstanceOf(IllegalArgumentException.class)
                                .hasMessage("Admin s ovim e-mailom već postoji.");

        verify(adminRepository, never()).save(Mockito.any(Admin.class));

    }

}
