package com.example.userservice.config;


import com.example.userservice.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// User-service SecurityConfig (basic auth, password encoder etc.)
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class UserSecurityConfig {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

  /*  @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/users/login", "/api/v1/users/register").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }*/
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      return http
              .csrf(csrf -> csrf.disable())
              .authorizeHttpRequests(auth -> auth
                      .requestMatchers("/api/v1/users/login", "/api/v1/users/register").permitAll()
                      .requestMatchers("/api/v1/users/all").hasRole("USER")
              )
              .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
              .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
              .build();
  }


}
