package com.vocacional.orientacionvocacional.config;

import com.vocacional.orientacionvocacional.Filter.JwtRequestFilter;
import com.vocacional.orientacionvocacional.model.enums.ERole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/login", "/auth/register",
                                "/auth/registerStudent", "/auth/registerAdviser",
                                "/students/verify", "/students/resend-verification-code"
                        ).permitAll()
                        .requestMatchers(
                                "/students/**",
                                "/api/v1/adviser/getAdviser/**",
                                "/adviser/listAdvisors",
                                "/api/v1/checkout/**",
                                "/api/v1/availability/**",
                                "/api/v1/students/{id}/cancel-plan"
                        ).hasAnyAuthority(ERole.STUDENT.name(), ERole.ADVISER.name(), ERole.ADMIN.name())
                        .requestMatchers("/adviser/**").hasAnyAuthority(ERole.ADVISER.name(), ERole.STUDENT.name(), ERole.ADMIN.name())
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .cors().configurationSource(corsConfigurationSource());

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
      configuration.addAllowedOrigin("https://orientacion-vocacional.vercel.app"); // Origen permitido
        configuration.addAllowedMethod("*");  // Permite todos los métodos (GET, POST, DELETE, etc.)
        configuration.addAllowedHeader("*");  // Permite todos los encabezados
        configuration.setAllowCredentials(true);  // Permite credenciales como cookies y encabezados de autenticación

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // Aplica a todas las rutas
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}