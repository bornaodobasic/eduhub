package fer.progi.backend.service;

import fer.progi.backend.domain.VrijemeSata;

import java.util.List;
import java.util.Optional;

public interface VrijemeSataService {

    List<VrijemeSata> listAll();
    Optional<VrijemeSata> findById(Integer id);
}
