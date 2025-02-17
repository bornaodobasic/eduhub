package fer.progi.backend.service.impl;

import fer.progi.backend.dao.VrijemeSataRepository;
import fer.progi.backend.domain.VrijemeSata;
import fer.progi.backend.service.VrijemeSataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VrijemeSataServiceJpa implements VrijemeSataService {

    @Autowired
    private VrijemeSataRepository vrijemeSataRepo;

    @Override
    public List<VrijemeSata> listAll() {
        return vrijemeSataRepo.findAll();
    }

    @Override
    public Optional<VrijemeSata> findById(Integer id) {
        return vrijemeSataRepo.findById(id);
    }

}
