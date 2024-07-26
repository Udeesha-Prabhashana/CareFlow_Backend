package com.example.careflow_backend.config.jwtConfig;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomJwtAuthenticationConverter extends JwtAuthenticationConverter {
    protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        Collection<GrantedAuthority> authorities = grantedAuthoritiesConverter.convert(jwt);

        // Add ROLE_ prefix if not present
        return authorities.stream()
                .map(grantedAuthority -> {
                    String authority = grantedAuthority.getAuthority();
                    if (!authority.startsWith("ROLE_")) {
                        authority = "ROLE_" + authority;
                    }
                    return new SimpleGrantedAuthority(authority);
                })
                .collect(Collectors.toList());
    }
}

