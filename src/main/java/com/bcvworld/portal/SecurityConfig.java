package com.bcvworld.portal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(
            JwtAuthFilter jwtAuthFilter,
            CorsConfigurationSource corsConfigurationSource
    ) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // REST API → CSRF disabled
            .csrf(csrf -> csrf.disable())

            // Use our CORS config
            .cors(cors -> cors.configurationSource(corsConfigurationSource))

            // JWT = Stateless
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Authorization rules
            .authorizeHttpRequests(auth -> auth

                // Preflight
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // 🔓 PUBLIC APIs
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/jobs/**").permitAll()
                .requestMatchers("/api/companies/logos/**").permitAll()
                .requestMatchers("/api/admin/auth/**").permitAll()

                // 🔐 ADMIN APIs
                .requestMatchers("/api/admin/jobs/**").hasRole("ADMIN")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // Everything else needs auth
                .anyRequest().authenticated()
            )

            // JWT filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
