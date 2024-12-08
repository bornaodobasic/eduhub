package fer.progi.backend.rest;

import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.service.AktivnostService;
import fer.progi.backend.service.UcenikService;
import fer.progi.backend.service.impl.UcenikServiceJpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ucenik")
public class UcenikController {

    @Autowired
    private UcenikService ucenikService;


}
