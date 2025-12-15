package com.minsu.webservice.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minsu.webservice.global.security.CustomUserDetailsService;
import com.minsu.webservice.global.security.JwtAuthenticationFilter;
import com.minsu.webservice.global.security.JwtProperties;
import com.minsu.webservice.global.security.JwtProvider;
import com.minsu.webservice.global.security.JsonAccessDeniedHandler;
import com.minsu.webservice.global.security.JsonAuthenticationEntryPoint;
import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtProvider jwtProvider(JwtProperties props) {
        return new JwtProvider(props);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*"); // Allow all origins for testing
        configuration.addAllowedMethod("*"); // Allow all HTTP methods
        configuration.addAllowedHeader("*"); // Allow all headers
        configuration.setAllowCredentials(true); // Allow credentials (e.g., cookies, authorization headers)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply this CORS configuration to all paths
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtProvider jwtProvider,
                                           CustomUserDetailsService userDetailsService,
                                           ObjectMapper objectMapper) throws Exception {

        var jwtFilter = new JwtAuthenticationFilter(jwtProvider, userDetailsService);

        return http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults()) // Apply CORS configuration
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint(new JsonAuthenticationEntryPoint(objectMapper))
                        .accessDeniedHandler(new JsonAccessDeniedHandler(objectMapper))
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/health",
                                "/auth/login",
                                "/auth/refresh",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/books",
                                "/reviews/*",
                                "/books/*/reviews"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
