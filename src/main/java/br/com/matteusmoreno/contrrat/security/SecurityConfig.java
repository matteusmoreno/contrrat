package br.com.matteusmoreno.contrrat.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
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

                        // --- INÍCIO DA ALTERAÇÃO (DEBUG) ---
                        // Temporariamente permitir todas as requisições POST para /contracts
                        .requestMatchers(HttpMethod.POST, "/contracts").permitAll()
                        .requestMatchers("/contracts/my-contracts-as-customer").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers("/contracts/confirm/**", "/contracts/reject/**").hasAuthority("ROLE_ARTIST")
                        .requestMatchers("/contracts/my-contracts-as-artist").hasAuthority("ROLE_ARTIST")
                        // --- FIM DA ALTERAÇÃO (DEBUG) ---

                        // Regra para um artista gerenciar sua própria disponibilidade
                        .requestMatchers(HttpMethod.POST, "/availability").hasAuthority("ROLE_ARTIST")
                        .requestMatchers(HttpMethod.PUT, "/availability/update").hasAuthority("ROLE_ARTIST")
                        .requestMatchers(HttpMethod.PATCH, "/availability/change-status/**").hasAuthority("ROLE_ARTIST")

                        // Qualquer outra requisição precisa de autenticação
                        .anyRequest().authenticated())

                .oauth2Login(oauth2 -> oauth2.successHandler(customOAuth2AuthenticationSuccessHandler))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
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
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}