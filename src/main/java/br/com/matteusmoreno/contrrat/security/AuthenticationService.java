package br.com.matteusmoreno.contrrat.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    public Jwt getPrincipal() {
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String getAuthenticatedUserId() {
        return getPrincipal().getClaimAsString("userId");
    }

    public String getAuthenticatedArtistId() {
        String artistId = getPrincipal().getClaimAsString("artistId");
        if (artistId == null) {
            throw new AccessDeniedException("User is not an artist");
        }
        return artistId;
    }

    public String getAuthenticatedCustomerId() {
        String customerId = getPrincipal().getClaimAsString("customerId");
        if (customerId == null) {
            throw new AccessDeniedException("User is not a customer");
        }
        return customerId;
    }
}