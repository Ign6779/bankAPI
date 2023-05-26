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
        /* The token will look something like this

        {
          "sub": "admin",
          "auth": [
            {
              "role": "ROLE_ADMIN"
            }
          ],
          "iat": 1684073744,
          "exp": 1684077344
        }

        */

        // We create a new Claims object for the token
        // The username is the subject
        Claims claims = Jwts.claims().setSubject(username);

        // And we add an array of the roles to the auth element of the Claims
        // Note that we only provide the role as information to the frontend
        // The actual role based authorization should always be done in the backend code
        claims.put("auth",
                roles
                        .stream()
                        .map(Role::name)
                        .toList());

        // We decide on an expiration date
        Date now = new Date();
        Date expiration = new Date(now.getTime() + validityInMicroseconds);

        // And finally, generate the token and sign it. .compact() then turns it into a string that we can return.
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
