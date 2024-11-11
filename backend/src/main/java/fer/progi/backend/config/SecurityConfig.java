package fer.progi.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // Disable CSRF for simplicity
            .authorizeRequests()
            .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/ucenici/**").permitAll()// Allow access to H2 Console
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
            .headers().frameOptions().sameOrigin();  // Allow frames (needed for H2 Console)
        return http.build();
    }
}