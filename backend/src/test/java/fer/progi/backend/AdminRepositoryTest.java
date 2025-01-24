package fer.progi.backend;

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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminRepositoryTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminServiceJpa adminService;

    @Test
    public void test() {
        Admin admin = new Admin();
        admin.setPrezimeAdmin("prezime");
        admin.setImeAdmin("ime");
        admin.setEmail("email@eduhub.com");

        AddDTO addDTO = new AddDTO();
        addDTO.setPrezime("prezime");
        addDTO.setIme("ime");
        addDTO.setEmail("email@eduhub.com");

        when(adminRepository.save(Mockito.any(Admin.class))).thenReturn(admin);
        Admin savedAdmin = adminService.addAdmin(addDTO);

        Assertions.assertThat(savedAdmin).isNotNull();
        Assertions.assertThat(savedAdmin.getPrezimeAdmin()).isEqualTo("prezime");
        Assertions.assertThat(savedAdmin.getImeAdmin()).isEqualTo("ime");
        Assertions.assertThat(savedAdmin.getEmail()).isEqualTo("email@eduhub.com");

        verify(adminRepository, times(1)).save(Mockito.any(Admin.class));


    }

}
