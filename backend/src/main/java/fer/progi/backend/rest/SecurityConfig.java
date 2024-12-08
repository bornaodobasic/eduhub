package fer.progi.backend.rest;

import fer.progi.backend.domain.Admin;
import fer.progi.backend.domain.Djelatnik;
import fer.progi.backend.domain.Nastavnik;
import fer.progi.backend.domain.Ravnatelj;
import fer.progi.backend.domain.Satnicar;
import fer.progi.backend.domain.Ucenik;
import fer.progi.backend.service.impl.AdminServiceJpa;
import fer.progi.backend.service.impl.NastavnikServiceJpa;
import fer.progi.backend.service.impl.UcenikServiceJpa;
import fer.progi.backend.service.impl.DjelatnikServiceJpa;
import fer.progi.backend.service.impl.SatnicarServiceJpa;
import fer.progi.backend.service.impl.RavnateljServiceJpa;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import java.io.IOException;
import java.util.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private AdminServiceJpa adminService;
    @Autowired
    private NastavnikServiceJpa nastavnikService;
    @Autowired
    private DjelatnikServiceJpa djelatnikService;
    @Autowired
    private UcenikServiceJpa ucenikService;
    @Autowired
    private RavnateljServiceJpa ravnateljService;
    @Autowired
    private SatnicarServiceJpa satnicarService;


    @Bean
    public LogoutHandler customLogoutHandler() {
        return new CustomLogoutHandler();
    }

    @Bean
    public LogoutSuccessHandler customLogoutSuccessHandler() {
        return (request, response, authentication) -> {
            System.out.println("Logout uspješan");
            String log = "https://login.microsoftonline.com/a983c51c-e23d-4e05-b97e-fd9ccf9476c8/oauth2/v2.0/logout";
            // PROMIJENITI U DEPLOYU
            String post1 = "?post_logout_redirect_uri=http%3A%2F%2Flocalhost%3A8080%2F";

            response.sendRedirect(log + post1);
        };
    }



    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

                Object principal = authentication.getPrincipal();
                Collection<GrantedAuthority> updatedAuthorities = new HashSet<>();
                String role = new String();
                String email = new String();
                boolean exists = false;

                if (principal instanceof Jwt) {
                    Jwt jwt = (Jwt) principal;
                    List<String> roles = jwt.getClaimAsStringList("roles");
                    role = roles != null && !roles.isEmpty() ? roles.get(0) : null;

                    email = jwt.getClaimAsString("preferred_username");
                    System.out.println(email);

                    if (role != null) {
                        updatedAuthorities.add(new SimpleGrantedAuthority(role));
                    }

                    if (role.equals("Admin")) {
                        Admin admin = adminService.getOrCreateAdmin(email);

                    } else if (role.equals("Nastavnik")) {
                        Nastavnik nastavnik = nastavnikService.getOrCreateNastavnik(email);

                    } else if (role.equals("Djelatnik")) {
                        Djelatnik djelatnik = djelatnikService.getOrCreateDjelatnik(email);

                    } else if (role.equals("Satnicar")) {
                        Satnicar satnicar = satnicarService.getOrCreateSatnicar(email);

                    } else if (role.equals("Ucenik")) {
                        exists = ucenikService.existsByEmail(email);
                        if (exists) {
                            Optional<Ucenik> ucenik  = ucenikService.findByEmail(email);
                        }

                    } else if (role.equals("Ravnatelj")) {
                        Ravnatelj ravnatelj  = ravnateljService.getOrCreateRavnatelj(email);
                     }

                    updatedAuthorities.addAll(authentication.getAuthorities());

                } else if (principal instanceof OidcUser) {
                    OidcUser oidcUser = (OidcUser) principal;
                    List<String> roles = (List<String>) oidcUser.getAttributes().get("roles");

                    role = roles != null && !roles.isEmpty() ? roles.get(0) : null;

                    email = (String) oidcUser.getAttributes().get("preferred_username");
                    System.out.println(email);
                    System.out.println(role);

                    if (role != null) {
                        updatedAuthorities.add(new SimpleGrantedAuthority(role));
                    }

                    if (role.equals("Admin")) {
                        Admin admin = adminService.getOrCreateAdmin(email);
                    }

                    if (role.equals("Nastavnik")) {
                        Nastavnik nastavnik = nastavnikService.getOrCreateNastavnik(email);
                    }

                    if (role.equals("Ucenik")) {
                       exists = ucenikService.existsByEmail(email);
                       if (exists) {
                           Optional<Ucenik> ucenik  = ucenikService.findByEmail(email);
                       }

                    }

                    if (role.equals("Ravnatelj")) {
                        Ravnatelj ravnatelj  = ravnateljService.getOrCreateRavnatelj(email);
                     }

                    if (role.equals("Satnicar")) {
                        Satnicar satnicar = satnicarService.getOrCreateSatnicar(email);
                     }

                    if (role.equals("Djelatnik")) {
                        Djelatnik djelatnik  = djelatnikService.getOrCreateDjelatnik(email);
                     }

                    updatedAuthorities.addAll(authentication.getAuthorities());
                }

                Authentication newAuth = new UsernamePasswordAuthenticationToken(
                        authentication.getPrincipal(),
                        authentication.getCredentials(),
                        updatedAuthorities
                );



                SecurityContextHolder.getContext().setAuthentication(newAuth);

                System.out.println("Updated Authorities in SecurityContext:");
                SecurityContext context = SecurityContextHolder.getContext();
                context.getAuthentication().getAuthorities().forEach(System.out::println);


                if (updatedAuthorities.stream().anyMatch(auth -> auth.getAuthority().equals("Admin"))) {
                    response.sendRedirect("/admin/zahtjevi/tempAdmin");
                } else if (updatedAuthorities.stream().anyMatch(auth -> auth.getAuthority().equals("Nastavnik"))) {
                    response.sendRedirect("/nastavnik");
                } else if (updatedAuthorities.stream().anyMatch(auth -> auth.getAuthority().equals("Djelatnik"))) {
                    response.sendRedirect("/djelatnik");
                } else if (updatedAuthorities.stream().anyMatch(auth -> auth.getAuthority().equals("Satnicar"))) {
                    response.sendRedirect("/satnicar");
                } else if (updatedAuthorities.stream().anyMatch(auth -> auth.getAuthority().equals("Ravnatelj"))) {
                    response.sendRedirect("/ravnatelj");
                } else if (updatedAuthorities.stream().anyMatch(auth -> auth.getAuthority().equals("Ucenik")) && exists) {
                    response.sendRedirect("/ucenik");
                } else if (updatedAuthorities.stream().anyMatch(auth -> auth.getAuthority().equals("Ucenik")) && !exists){
                    response.sendRedirect("/upis");
                } else {
                    response.sendRedirect("/home");
                }
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeRequests(auth -> auth
                        .requestMatchers("/ouath2/authorization/**", "/login/**", "/static/**", "/index.html", "/", "/favicon.ico", "/logo192.png", "/manifest.json", "/h2-console/**").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("Admin")
                        .requestMatchers("/nastavnik/**").hasAuthority("Nastavnik")
                        .requestMatchers("/djelatnik/**").hasAuthority("Djelatnik")
                        .requestMatchers("/ravnatelj/**").hasAuthority("Ravnatelj")
                        .requestMatchers("/satnicar/**").hasAuthority("Satnicar")
                        .requestMatchers("/ucenik/**").hasAuthority("Ucenik")
                        .requestMatchers("/upis").hasAuthority("Ucenik")
                        .anyRequest().authenticated()
                )
                .csrf()
                .ignoringRequestMatchers("/h2-console/**")
                .and()
                    .headers()
                    .frameOptions()
                    .sameOrigin()
                .and()
                .csrf().disable()
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/azure-dev")
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService()))
                        .successHandler(customSuccessHandler())
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                        .addLogoutHandler(customLogoutHandler())
                        .logoutSuccessHandler(customLogoutSuccessHandler())
                );

        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
        return userRequest -> {
            OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

            Collection<GrantedAuthority> authorities = new HashSet<>();
            List<String> roles = (List<String>) oAuth2User.getAttributes().get("roles");

            if (roles != null) {
                for (String role : roles) {
                    System.out.println(role);
                    authorities.add(new SimpleGrantedAuthority(role));
                }
            }

            return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), "sub");
        };
    }

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        return new Converter<Jwt, AbstractAuthenticationToken>() {
            @Override
            public AbstractAuthenticationToken convert(Jwt jwt) {
                List<String> roles = jwt.getClaimAsStringList("roles");

                Collection<GrantedAuthority> authorities = new HashSet<>();
                if (roles != null) {
                    for (String role : roles) {
                        System.out.println(role);
                        authorities.add(new SimpleGrantedAuthority(role));
                    }
                }
                return new JwtAuthenticationToken(jwt, authorities);
            }
        };
    }

    @Bean
    public JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("ROLE_");
        converter.setAuthoritiesClaimName("roles");
        return converter;
    }

}
