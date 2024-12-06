package fer.progi.backend.rest;

import fer.progi.backend.domain.Admin;
import fer.progi.backend.service.AdminService;
import fer.progi.backend.service.impl.AdminServiceJpa;
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


import java.io.IOException;
import java.util.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private AdminServiceJpa adminService;

    /*
    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return (request, response, authentication) -> {
            Collection<? extends GrantedAuthority> currentAuthorities = authentication.getAuthorities();
            List<GrantedAuthority> updatedAuthorities = new ArrayList<>(currentAuthorities);
            updatedAuthorities.add(new SimpleGrantedAuthority("Admin"));

            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    authentication.getPrincipal(),
                    authentication.getCredentials(),
                    updatedAuthorities
            );

            SecurityContextHolder.getContext().setAuthentication(newAuth);

            System.out.println("Updated Authorities in SecurityContext:");
            SecurityContext context = SecurityContextHolder.getContext();
            context.getAuthentication().getAuthorities().forEach(System.out::println);


            // Redirect based on roles
            if (updatedAuthorities.stream().anyMatch(auth -> auth.getAuthority().equals("Admin"))) {
                response.sendRedirect("/admin/zahtjevi/tempAdmin");
            } else {
                response.sendRedirect("/home");
            }
        };
    }
*/
    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

                Object principal = authentication.getPrincipal();
                Collection<GrantedAuthority> updatedAuthorities = new HashSet<>();
                String role = new String();
                String email = new String();

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
                } else if (updatedAuthorities.stream().anyMatch(auth -> auth.getAuthority().equals("Ucenik"))) {
                    response.sendRedirect("/ucenik");
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
                        .anyRequest().authenticated()
                )
                .csrf()
                .ignoringRequestMatchers("/h2-console/**")
                .and()
                    .headers()
                    .frameOptions()
                    .sameOrigin()
                .and()
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/azure-dev")
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService())) // Use custom OAuth2 user service
                        .successHandler(customSuccessHandler()) // Use custom success handler
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
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
