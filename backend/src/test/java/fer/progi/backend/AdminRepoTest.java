package fer.progi.backend;

import fer.progi.backend.dao.AdminRepository;
import fer.progi.backend.domain.Admin;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AdminRepoTest {

    @Autowired
    private AdminRepository adminRepository;

    @Test
    public void testSaveAdmin() {
        Admin admin = new Admin();
        admin.setImeAdmin("ime");
        admin.setEmail("email@eduxhub.onmicrosoft.com");
        admin.setPrezimeAdmin("prezime");

        // Saving the admin entity
        Admin savedAdmin = adminRepository.save(admin);

        // Assertions
        Assertions.assertThat(savedAdmin).isNotNull();
        Assertions.assertThat(savedAdmin.getId()).isGreaterThan(0);
    }
}
