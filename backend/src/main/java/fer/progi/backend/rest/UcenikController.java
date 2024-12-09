package fer.progi.backend.rest;

import fer.progi.backend.service.UcenikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ucenik")
public class UcenikController {

    @Autowired
    private UcenikService ucenikService;


}
