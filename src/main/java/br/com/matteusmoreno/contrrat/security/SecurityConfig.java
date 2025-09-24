package br.com.matteusmoreno.contrrat.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler;

    public SecurityConfig(CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler) {
        this.customOAuth2AuthenticationSuccessHandler = customOAuth2AuthenticationSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints Públicos
                        .requestMatchers("/login/**", "/oauth2/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/artists", "/customers").permitAll()
                        .requestMatchers(HttpMethod.GET, "/artists/**", "/availability/**", "/signature/**", "/artists/artistic-fields", "/artists/artists-by-field/**", "/artists/premium-artists", "/artists/all-active").permitAll()

                        // Regras para Contratos
                        .requestMatchers(HttpMethod.POST, "/contracts").hasAuthority("SCOPE_CUSTOMER") // Usar hasAuthority com prefixo SCOPE_
                        .requestMatchers("/contracts/my-contracts-as-customer").hasAuthority("SCOPE_CUSTOMER")
                        .requestMatchers("/contracts/confirm/**", "/contracts/reject/**").hasAuthority("SCOPE_ARTIST")
                        .requestMatchers("/contracts/my-contracts-as-artist").hasAuthority("SCOPE_ARTIST")

                        // Regra para um artista gerenciar sua própria disponibilidade
                        .requestMatchers(HttpMethod.POST, "/availability").hasAuthority("SCOPE_ARTIST")
                        .requestMatchers(HttpMethod.PUT, "/availability/update").hasAuthority("SCOPE_ARTIST")
                        .requestMatchers(HttpMethod.PATCH, "/availability/change-status/**").hasAuthority("SCOPE_ARTIST")


                        // Qualquer outra requisição precisa de autenticação
                        .anyRequest().authenticated())

                .oauth2Login(oauth2 -> oauth2.successHandler(customOAuth2AuthenticationSuccessHandler))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3001", "http://localhost:3002", "http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica para todos os endpoints
        return source;
    }
}