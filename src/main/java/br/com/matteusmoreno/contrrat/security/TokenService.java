package br.com.matteusmoreno.contrrat.security;

import br.com.matteusmoreno.contrrat.user.User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(User user) {
        Instant expiresAt = Instant.now().plusSeconds(60 * 60 * 24);

        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .subject(user.getUsername())
                .claim("userId", user.getUserId())
                .claim("scope", user.getProfile().name())
                .claim("name", user.getName())
                .issuedAt(Instant.now())
                .expiresAt(expiresAt);

        // Adiciona a claim específica do perfil
        if (user.getArtistId() != null) claimsBuilder.claim("artistId", user.getArtistId());
        if (user.getCustomerId() != null) claimsBuilder.claim("customerId", user.getCustomerId());

        return jwtEncoder.encode(JwtEncoderParameters.from(claimsBuilder.build())).getTokenValue();
    }
}