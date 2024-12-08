package fer.progi.backend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import fer.progi.backend.domain.Admin;
import fer.progi.backend.service.AdminService;


@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('Admin')")
public class AdminController {
	
	@Autowired
	private AdminService AdminService;
	
	@PostMapping("/add")
	public Admin dodajAdmin(@RequestBody Admin admin) {
		return AdminService.dodajAdmin(admin);
	}

}


