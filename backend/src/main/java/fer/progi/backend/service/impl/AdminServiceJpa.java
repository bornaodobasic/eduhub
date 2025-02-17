package fer.progi.backend.service.impl;

import fer.progi.backend.dao.*;
import fer.progi.backend.domain.*;
import fer.progi.backend.dto.AddDTO;
import fer.progi.backend.dto.AdminAddUcenikDTO;
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
        Optional<Admin> optionalAdmin = adminRepo.findByEmail(addDTO.getEmail());
        if (optionalAdmin.isPresent()) {
            throw new IllegalArgumentException("Admin s ovim e-mailom već postoji.");
        }
        if (addDTO.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email ne može biti null.");
        }
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
        djelatnik.setPrezimeDjel(addDTO.getPrezime());
        djelatnik.setEmail(addDTO.getEmail());

        return djelatnikRepo.save(djelatnik);
    }

    @Override
    public Satnicar addSatnicar(AddDTO addDTO) {
        Satnicar satnicar = new Satnicar();
        Optional<Satnicar> optionalSatnicar = satnicarRepo.findByEmail(addDTO.getEmail());
        if (optionalSatnicar.isPresent()) {
            throw new IllegalArgumentException("Satnicar s ovim e-mailom već postoji.");
        }

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
    public boolean createIfNeeded(AddDTO addDTO) {
        Optional<Admin> optionalAdmin = adminRepo.findByEmail(addDTO.getEmail());
        if (optionalAdmin.isEmpty()) {
            Admin admin = new Admin();
            admin.setEmail(addDTO.getEmail());
            admin.setImeAdmin(addDTO.getIme());
            admin.setPrezimeAdmin(addDTO.getPrezime());
            adminRepo.save(admin);
        }
        return true;
    }

    @Override
    public Ucenik addUcenik(AdminAddUcenikDTO adminAddUcenikDTO) {

        Optional<Ucenik> optionalUcenik = ucenikRepo.findByEmail(adminAddUcenikDTO.getEmail());
        if(optionalUcenik.isPresent()) {
            throw new IllegalArgumentException("Ucenik s e-mailom već postoji.");
        }

        Razred optionalRazred = razredService.findByNazivRazred(adminAddUcenikDTO.getRazred());
        if (optionalRazred == null) {
            throw new IllegalArgumentException("Razred s ovim nazivom ne postoji.");
        }

        Ucenik ucenik = new Ucenik();
        ucenik.setSpol(adminAddUcenikDTO.getSpol());
        ucenik.setImeUcenik(adminAddUcenikDTO.getImeUcenik());
        ucenik.setOib(adminAddUcenikDTO.getOib());
        ucenik.setPrezimeUcenik(adminAddUcenikDTO.getPrezimeUcenik());
        ucenik.setDatumRodenja(adminAddUcenikDTO.getDatumRodenja());
        ucenik.setRazred(optionalRazred);
        ucenik.setEmail(adminAddUcenikDTO.getEmail());
        ucenik.setVjeronauk(adminAddUcenikDTO.getVjeronauk());

        return ucenikRepo.save(ucenik);
    }

    public void deleteAdminByName(String name) {
        //Samo za potrebe testiranja
        throw new UnsupportedOperationException("Metoda nije implementirana.");
    }

}
