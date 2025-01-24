package fer.progi.backend.component;

import fer.progi.backend.service.impl.AdminServiceJpa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NonExistingFunctionalityTest {

    @Test
    public void testDeleAdminByNameNotImplemented() {

        AdminServiceJpa adminService = mock(AdminServiceJpa.class);

        doThrow(new UnsupportedOperationException("Metoda nije implementirana."))
                .when(adminService).deleteAdminByName("Nikola");

        assertThatThrownBy(() -> adminService.deleteAdminByName("Nikola"))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Metoda nije implementirana.");

        verify(adminService, times(1)).deleteAdminByName("Nikola");
    }

}
