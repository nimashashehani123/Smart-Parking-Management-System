package com.example.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/api/v1/users/login",
                                "/api/v1/users/register",
                                "/api/v1/parkings/available",
                                "/api/v1/parkings/zone/**",
                                "/api/v1/reservations/**",
                                "/api/v1/reservations/status/**",
                                "/api/v1/reservations/user/active/**",
                                "/api/v1/parkings/reserve/**",
                                "/api/v1/parkings/release/**",
                                "/api/v1/parkings/get/**"
                        ).permitAll()
                        .pathMatchers("/api/v1/users/all").hasRole("OWNER")
                        .pathMatchers("/api/v1/users/updateUser/**").hasRole("USER")
                        .pathMatchers("/api/v1/users/getUser/**").hasRole("USER")
                        .pathMatchers("/api/v1/users/bookings/**").hasRole("USER")
                        .pathMatchers("/api/v1/vehicles/user/**").hasRole("USER")
                        .pathMatchers("/api/v1/vehicles/add").hasRole("USER")
                        .pathMatchers("/api/v1/vehicles/getAll").hasRole("OWNER")
                        .pathMatchers("/api/v1/vehicles/**").hasRole("USER")
                        .pathMatchers("/api/v1/parkings/add").hasRole("OWNER")
                        .pathMatchers("/api/v1/parkings/update").hasRole("OWNER")
                        .pathMatchers("/api/v1/parkings/status/**").hasRole("OWNER")
                        .pathMatchers("/api/v1/reservations/create").hasRole("USER")
                        .pathMatchers("/api/v1/reservations/cancel/**").hasRole("USER")
                        .pathMatchers("/api/v1/reservations/user/**").hasRole("USER")
                        .pathMatchers("/api/v1/payments/make").hasRole("USER")
                        .pathMatchers("/api/v1/payments/receipt/**").hasRole("USER")
                        .anyExchange().authenticated()

                )
                .addFilterBefore(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
