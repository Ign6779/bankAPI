package nl.inholland.bankapi.util;

import io.jsonwebtoken.JwtException;
import lombok.Value;
import nl.inholland.bankapi.models.Role;
import nl.inholland.bankapi.services.MyUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import java.util.List;
import java.util.Date;
import java.security.PrivateKey;

@Component
public class JwtTokenProvider {
    @Value("${application.token.validity}")
    private long validityInMicroseconds;
    private final MyUserDetailsService myUserDetailsService;
    private final JwtKeyProvider jwtKeyProvider;

    public JwtTokenProvider(MyUserDetailsService myUserDetailsService, JwtKeyProvider jwtKeyProvider) {
        this.myUserDetailsService = myUserDetailsService;
        this.jwtKeyProvider = jwtKeyProvider;
    }

    public String CreateToken(String username, List<Role> roles) throws JwtException {

        Claims claims = Jwts.claims().setSubject(username);

        claims.put("auth",
                roles
                        .stream()
                        .map(Role::name)
                        .toList());

        Date now = new Date();
        Date expiration = new Date(now.getTime() + validityInMicroseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(jwtKeyProvider.getPrivateKey())
                .compact();
    }

    public Authentication getAuthentication(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(jwtKeyProvider.getPrivateKey()).build().parseClaimsJws(token);
            String username = claims.getBody().getSubject();
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Bearer token not valid");
        }
    }
}
