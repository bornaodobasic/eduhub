package fer.progi.backend.rest;

import fer.progi.backend.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import fer.progi.backend.service.AdminService;


@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('Admin')")
public class AdminController {

    @Autowired
    private AdminService AdminService;

    @PostMapping("/add/admin")
    public Admin addAdmin(@RequestBody AddDTO addDTO) {
        return AdminService.addAdmin(addDTO);
    }

    @PostMapping("/add/djelatnik")
    public Djelatnik addDjelatnik(@RequestBody AddDTO addDTO) {return AdminService.addDjelatnik(addDTO); }

    @PostMapping("/add/satnicar")
    public Satnicar addSatnicar(@RequestBody AddDTO addDTO) {
        return AdminService.addSatnicar(addDTO);
    }

    @PostMapping("/add/nastavnik")
    public Nastavnik addNastavnik(@RequestBody AddDTO addDTO) {
        return AdminService.addNastavnik(addDTO);
    }

    @PostMapping("/add/ravnatelj")
    public Ravnatelj addRavnatelj(@RequestBody AddDTO addDTO) {
        return AdminService.addRavnatelj(addDTO);
    }

}


