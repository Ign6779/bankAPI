package nl.inholland.bankapi.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import nl.inholland.bankapi.models.Role;
import nl.inholland.bankapi.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {
    private final MyUserDetailsService myUserDetailsService;
    private final JwtKeyProvider jwtKeyProvider;
    @Value("${application.token.validity}")
    private long validityInMicroseconds;

    public JwtTokenProvider(MyUserDetailsService myUserDetailsService, JwtKeyProvider jwtKeyProvider) {
        this.myUserDetailsService = myUserDetailsService;
        this.jwtKeyProvider = jwtKeyProvider;
    }

    public String CreateToken(String email, List<Role> roles) throws JwtException {

        Claims claims = Jwts.claims().setSubject(email);

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
            String email = claims.getBody().getSubject();
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(email);
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Bearer token not valid");
        }
    }
}
