
package fer.progi.backend.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fer.progi.backend.domain.Admin;
import fer.progi.backend.service.AdminService;


@RestController
@RequestMapping("/admini")
public class AdminController {
	
	@Autowired
	private AdminService AdminService;
	
	@GetMapping("")
	public List<Admin> listAdmin() {
		return AdminService.listAll();
	}
	
	@PostMapping("")
	public Admin dodajAdmin(@RequestBody Admin admin) {
		return AdminService.dodajAdmin(admin);
	}

}
