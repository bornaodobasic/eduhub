package fer.progi.backend.rest;

import fer.progi.backend.dto.AddDTO;
import fer.progi.backend.service.RazredService;
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
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import java.io.IOException;
import java.net.URLEncoder;
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

    @Autowired
    private RazredService razredService;


    @Bean
    public LogoutHandler customLogoutHandler() {
        return new CustomLogoutHandler();
    }

    @Bean
    public LogoutSuccessHandler customLogoutSuccessHandler() {
        return (request, response, authentication) -> {
            System.out.println("Logout uspješan");

            String microsoftLogout = "https://login.microsoftonline.com/a983c51c-e23d-4e05-b97e-fd9ccf9476c8/oauth2/v2.0/logout";
            String postLogoutRedirect = "http://localhost:8080";
            String logoutRedirect = microsoftLogout + "?post_logout_redirect_uri=" + URLEncoder.encode(postLogoutRedirect, "UTF-8");

            response.sendRedirect(logoutRedirect);
        };
    }


    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

                Object principal = authentication.getPrincipal();
                Collection<GrantedAuthority> updatedAuthorities = new HashSet<>();
                String role;
                String email;
                boolean present = false;
                boolean exists = true;
                AddDTO addDTO = new AddDTO();


                if (principal instanceof Jwt) {
                    Jwt jwt = (Jwt) principal;
                    List<String> roles = jwt.getClaimAsStringList("roles");
                    role = roles != null && !roles.isEmpty() ? roles.getFirst() : null;

                    email = jwt.getClaimAsString("preferred_username");
                    String userFullName = jwt.getClaimAsString("name");
                    System.out.println(email);
                    addDTO.setEmail(email);

                    int firstSpace = userFullName.indexOf(' ');
                    String firstName = userFullName.substring(0, firstSpace);
                    String lastName = userFullName.substring(firstSpace + 1);
                    addDTO.setIme(firstName);
                    addDTO.setPrezime(lastName);

                    if (role.equals("Admin")) {
                        present = adminService.createIfNeeded(addDTO);
                    } else if (role.equals("Nastavnik")) {
                        present = nastavnikService.createIfNeeded(addDTO);

                    } else if (role.equals("Djelatnik")) {
                        present = djelatnikService.createIfNeeded(addDTO);

                    } else if (role.equals("Satnicar")) {
                        present = satnicarService.createIfNeeded(addDTO);

                    } else if (role.equals("Ucenik")) {
                        exists = ucenikService.findByEmail(email);
                        present = true;

                    } else if (role.equals("Ravnatelj")) {
                        present  = ravnateljService.createIfNeeded(addDTO);
                    }

                    if (!exists) {
                        updatedAuthorities.add(new SimpleGrantedAuthority("Upis"));
                    } else if (present) {
                        updatedAuthorities.add(new SimpleGrantedAuthority(role));
                    }

                    updatedAuthorities.addAll(authentication.getAuthorities());

                } else if (principal instanceof OidcUser) {
                    OidcUser oidcUser = (OidcUser) principal;
                    List<String> roles = (List<String>) oidcUser.getAttributes().get("roles");

                    role = roles != null && !roles.isEmpty() ? roles.getFirst() : null;

                    email = (String) oidcUser.getAttributes().get("preferred_username");
                    String userFullName = (String) oidcUser.getAttributes().get("name");


                    addDTO.setEmail(email);
                    int firstSpace = userFullName.indexOf(' ');
                    String firstName = userFullName.substring(0, firstSpace);
                    String lastName = userFullName.substring(firstSpace + 1);
                    addDTO.setIme(firstName);
                    addDTO.setPrezime(lastName);

                    System.out.println(email);
                    System.out.println(role);

                    if (role.equals("Admin")) {
                        present = adminService.createIfNeeded(addDTO);
                    } else if (role.equals("Nastavnik")) {
                        present = nastavnikService.createIfNeeded(addDTO);

                    } else if (role.equals("Djelatnik")) {
                        present = djelatnikService.createIfNeeded(addDTO);

                    } else if (role.equals("Satnicar")) {
                        present = satnicarService.createIfNeeded(addDTO);

                    } else if (role.equals("Ucenik")) {
                        exists = ucenikService.findByEmail(email);
                        present = true;

                    } else if (role.equals("Ravnatelj")) {
                        present  = ravnateljService.createIfNeeded(addDTO);
                    }

                    if (!exists) {
                        updatedAuthorities.add(new SimpleGrantedAuthority("Upis"));
                    } else if (present) {
                        updatedAuthorities.add(new SimpleGrantedAuthority(role));
                    }

                    updatedAuthorities.addAll(authentication.getAuthorities());
                }

                SecurityContext context = SecurityContextHolder.getContext();
                context.getAuthentication().getAuthorities().forEach(System.out::println);

                Authentication newAuth = new UsernamePasswordAuthenticationToken(
                        authentication.getPrincipal(),
                        authentication.getCredentials(),
                        updatedAuthorities
                );



                SecurityContextHolder.getContext().setAuthentication(newAuth);

                System.out.println("Updated Authorities in SecurityContext:");
                context = SecurityContextHolder.getContext();
                context.getAuthentication().getAuthorities().forEach(System.out::println);

                if (updatedAuthorities.stream().anyMatch(auth -> auth.getAuthority().equals("Upis"))) {
                    response.sendRedirect("/upis");
                } else if (updatedAuthorities.stream().anyMatch(auth -> auth.getAuthority().equals("Admin"))) {
                    response.sendRedirect("/admin");
                } else if (updatedAuthorities.stream().anyMatch(auth -> auth.getAuthority().equals("Nastavnik"))) {
                    response.sendRedirect("/nastavnik");
                } else if (updatedAuthorities.stream().anyMatch(auth -> auth.getAuthority().equals("Djelatnik"))) {
                    response.sendRedirect("/djelatnik");
                } else if (updatedAuthorities.stream().anyMatch(auth -> auth.getAuthority().equals("Satnicar"))) {
                    response.sendRedirect("/satnicar");
                } else if (updatedAuthorities.stream().anyMatch(auth -> auth.getAuthority().equals("Ravnatelj"))) {
                    response.sendRedirect("/ravnatelj");
                } else if (updatedAuthorities.stream().anyMatch(auth -> auth.getAuthority().equals("Ucenik"))) {
                    response.sendRedirect("/ucenik");
                } else {
                    response.sendRedirect("/");
                }
            }
        };
    }

    @Bean
    public StrictHttpFirewall allowSemiColonInUrl() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowSemicolon(true);
        return firewall;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeRequests(auth -> auth
                		.requestMatchers("/ws/**", "/chat/**", "/chat2/**").permitAll()
                        .requestMatchers("/oauth2/authorization/**", "/login/**", "/static/**", "/index.html", "/", "/favicon.ico", "/logo192.png", "/manifest.json", "/h2-console/**").permitAll()
                        .requestMatchers("/api/admin/**").hasAuthority("Admin")
                        .requestMatchers("/api/nastavnik/**").hasAuthority("Nastavnik")
                        .requestMatchers("/api/djelatnik/**").hasAuthority("Djelatnik")
                        .requestMatchers("/api/ravnatelj/**").hasAuthority("Ravnatelj")
                        .requestMatchers("/api/satnicar/**").hasAuthority("Satnicar")
                        .requestMatchers("/api/ucenik/**").hasAuthority("Ucenik")
                        .requestMatchers("/upis").hasAuthority("Upis")
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
                )
                .setSharedObject(HttpFirewall.class, allowSemiColonInUrl());

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
