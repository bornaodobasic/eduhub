/*
package fer.progi.backend.rest;

import fer.progi.backend.dao.*;
import fer.progi.backend.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.springframework.security.core.authority.AuthorityUtils.NO_AUTHORITIES;
import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

@Service
public class UcenikUserDetailsService implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private NastavnikRepository nastavnikRepository;
    @Autowired
    private SatnicarRepository satnicarRepository;
    @Autowired
    private RavnateljRepository ravnateljRepository;
    @Autowired
    private DjelatnikRepository djelatnikRepository;
    @Autowired
    private UcenikRepository ucenikRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) {


        Admin admin = adminRepository.findByEmail(email);
        Nastavnik nastavnik = nastavnikRepository.findByEmail(email);
        Satnicar satnicar = satnicarRepository.findByEmail(email);
        Ravnatelj ravnatelj = ravnateljRepository.findByEmail(email);
        Djelatnik djelatnik = djelatnikRepository.findByEmail(email);
        Ucenik ucenik = ucenikRepository.findByEmail(email);

        if (admin != null) {
            return new User(
                    admin.getEmail(),
                    admin.getLozinka(),
                    commaSeparatedStringToAuthorityList("ROLE_administrator")
            );
        } else if (nastavnik != null){
            return new User(
                    nastavnik.getEmail(),
                    nastavnik.getLozinka(),
                    commaSeparatedStringToAuthorityList("ROLE_nastavnik")
            );
        } else if (satnicar != null){
            return new User(
                    satnicar.getEmail(),
                    satnicar.getLozinka(),
                    commaSeparatedStringToAuthorityList("ROLE_satnicar")
            );
        } else if (ravnatelj != null){
            return new User(
                    ravnatelj.getEmail(),
                    ravnatelj.getLozinka(),
                    commaSeparatedStringToAuthorityList("ROLE_ravnatelj")
            );
        } else if (djelatnik != null){
            return new User(
                    djelatnik.getEmail(),
                    djelatnik.getLozinka(),
                    commaSeparatedStringToAuthorityList("ROLE_djelatnik")
            );
        } else if (ucenik != null){
            return new User(
                    ucenik.getEmail(),
                    ucenik.getLozinka(),
                    commaSeparatedStringToAuthorityList("ROLE_ucenik")
            );
        } else {
            return new User(
                   "temp-email",
                    "$2a$12$qKib6i2WXz5Anu2/QD9qfu4HKILGT8H6.94wkzM3cZoXMv0FWrcI.",
                    commaSeparatedStringToAuthorityList("ROLE_GUEST")
            );
        }
        //else throw new UsernameNotFoundException("Nema korisnika" + email);
    }
}
*/