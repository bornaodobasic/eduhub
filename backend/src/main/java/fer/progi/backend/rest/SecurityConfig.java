package fer.progi.backend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final UcenikUserDetailsService ucenikUserDetailsService;

    public SecurityConfig(UcenikUserDetailsService ucenikUserDetailsService) {
        this.ucenikUserDetailsService = ucenikUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /*http
                .csrf().disable()
                .authorizeRequests()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/ucenici/**").permitAll()
                .requestMatchers("/ucionice/**").permitAll()
                .requestMatchers("/razredi/**").permitAll()
                .requestMatchers("/aktivnosti/**").permitAll()
                .requestMatchers("/smjerovi/**").permitAll()
                .requestMatchers("/predmeti/**").permitAll()
                .requestMatchers("/nastavnici/**").permitAll()
                .requestMatchers("/admini/**").permitAll()
                .requestMatchers("/ravnatelji/**").permitAll()
                .requestMatchers("/djelatnici/**").permitAll()
                .requestMatchers("/satnicari/**").permitAll()



                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().sameOrigin();*/

         http
                .csrf(csrf -> csrf.disable())  // Disable CSRF for all endpoints (consider enabling it for production)
                .authorizeRequests()
                //.anyRequest().permitAll()
                 .requestMatchers("/register/**").permitAll()
                 .requestMatchers("/loginUser/**").permitAll()
                 .requestMatchers("/h2-console/**").permitAll()
                 .requestMatchers("/upis/**").permitAll()
                 .anyRequest().authenticated()
                .and()
                .formLogin(withDefaults())
                .httpBasic(withDefaults());

        http.headers().frameOptions().sameOrigin();
    /*
        return http.build();
        http.authorizeHttpRequests(authz ->
                authz.anyRequest().permitAll());
        //authz.requestMatchers("/smjerovi/**").hasRole("administrator"));
        http.csrf(csrf -> csrf.disable());*/


        return http.build();
    }
}
/*
    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity(securedEnabled = true)
    public class SecurityConfig {

        @Autowired
        private UcenikUserDetailsService userDetailsService;

        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeRequests()
                    .requestMatchers("/smjerovi").hasRole("administrator")  // Ensure the admin role matches
                    .anyRequest().permitAll()
                    .and()
                    .formLogin()
                    .permitAll()
                    .and()
                    .httpBasic(); // If you're using basic auth for testing
        }

    }
    */

/*
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Autowired
    private UcenikUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Disable CSRF for all endpoints (consider enabling it for production)
                .authorizeRequests()
                // Allow access to the H2 console without authentication
                .requestMatchers("/h2-console/**").permitAll()
                // Allow access to /ucenici without authentication
                .requestMatchers("/ucenici/**").permitAll()
                // Restrict /smjerovi to users with ROLE_ADMIN
                .requestMatchers("/smjerovi/**").hasRole("administrator")
                // For everything else, permit all
                .anyRequest().permitAll()
                .and()
                // Enable form login for authentication
                .formLogin(withDefaults())  // Updated for Spring Security 6
                // Enable basic HTTP authentication for testing
                .httpBasic(withDefaults());  // Updated for Spring Security 6

        // Allow frames for the H2 console
        http.headers().frameOptions().sameOrigin();

        return http.build();
    }
}
*/

