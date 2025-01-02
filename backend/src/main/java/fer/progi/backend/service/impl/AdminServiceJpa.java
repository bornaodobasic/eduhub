package fer.progi.backend.service.impl;

import fer.progi.backend.dao.*;
import fer.progi.backend.domain.*;
import fer.progi.backend.rest.AddDTO;
import fer.progi.backend.rest.AdminAddUcenikDTO;
import fer.progi.backend.service.RazredService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fer.progi.backend.service.AdminService;

import java.util.List;
import java.util.Optional;


@Service
public class AdminServiceJpa implements AdminService {

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private DjelatnikRepository djelatnikRepo;

    @Autowired
    private NastavnikRepository nastavnikRepo;

    @Autowired
    private RavnateljRepository ravnateljRepo;

    @Autowired
    private SatnicarRepository satnicarRepo;

    @Autowired
    private RazredService razredService;

    @Autowired
    private UcenikRepository ucenikRepo;

    @Override
    public boolean findByEmail(String email) {
        return adminRepo.findByEmail(email).isPresent();
    }

    @Override
    public Admin addAdmin(AddDTO addDTO) {
        Admin admin = new Admin();
        admin.setImeAdmin(addDTO.getIme());
        admin.setPrezimeAdmin(addDTO.getPrezime());
        admin.setEmail(addDTO.getEmail());

        return adminRepo.save(admin);
    }

    @Override
    public List<Admin> findAllAdmins() {
        return adminRepo.findAll();
    }

    @Override
    public void deleteAdmin(String email) {
        Admin admin = adminRepo.findByEmail(email).orElse(null);
        adminRepo.delete(admin);
    }

    @Override
    public Djelatnik addDjelatnik(AddDTO addDTO) {
        Djelatnik djelatnik = new Djelatnik();
        djelatnik.setImeDjel(addDTO.getIme());
        djelatnik.setImeDjel(addDTO.getPrezime());
        djelatnik.setEmail(addDTO.getEmail());

        return djelatnikRepo.save(djelatnik);
    }

    @Override
    public Satnicar addSatnicar(AddDTO addDTO) {
        Satnicar satnicar = new Satnicar();
        satnicar.setImeSatnicar(addDTO.getIme());
        satnicar.setPrezimeSatnicar(addDTO.getPrezime());
        satnicar.setEmail(addDTO.getEmail());

        return satnicarRepo.save(satnicar);
    }

    @Override
    public Ravnatelj addRavnatelj(AddDTO addDTO) {
        Ravnatelj ravnatelj = new Ravnatelj();
        ravnatelj.setImeRavnatelj(addDTO.getIme());
        ravnatelj.setPrezimeRavnatelj(addDTO.getPrezime());
        ravnatelj.setEmail(addDTO.getEmail());

        return ravnateljRepo.save(ravnatelj);
    }

    @Override
    public Nastavnik addNastavnik(AddDTO addDTO) {
        Nastavnik nastavnik = new Nastavnik();
        nastavnik.setImeNastavnik(addDTO.getIme());
        nastavnik.setPrezimeNastavnik(addDTO.getPrezime());
        nastavnik.setEmail(addDTO.getEmail());

        return nastavnikRepo.save(nastavnik);
    }

    @Override
    public boolean createIfNeeded(String email) {
        Optional<Admin> optionalAdmin = adminRepo.findByEmail(email);
        if (optionalAdmin.isEmpty()) {
            Admin admin = new Admin();
            admin.setEmail(email);
            adminRepo.save(admin);
        }
        return true;
    }

    @Override
    public Ucenik addUcenik(AdminAddUcenikDTO adminAddUcenikDTO) {
        Ucenik ucenik = new Ucenik();
        ucenik.setSpol(adminAddUcenikDTO.getSpol());
        ucenik.setImeUcenik(adminAddUcenikDTO.getImeUcenik());
        ucenik.setOib(adminAddUcenikDTO.getOib());
        ucenik.setPrezimeUcenik(adminAddUcenikDTO.getPrezimeUcenik());
        ucenik.setDatumRodenja(adminAddUcenikDTO.getDatumRodenja());
        ucenik.setRazred(razredService.findRazred(adminAddUcenikDTO.getRazred()));
        ucenik.setEmail(adminAddUcenikDTO.getEmail());
        return ucenikRepo.save(ucenik);
    }



}
