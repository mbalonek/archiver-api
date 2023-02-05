package de.kenolab.security.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import de.kenolab.entity.ArchiveUser;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@Configuration
@Slf4j
public class UserAuthenticationProvider {
    @Value("${app.jwtSecret}")
    private String jwtKey;

    private final AuthenticationService authenticationService;

    public UserAuthenticationProvider(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostConstruct
    protected void init() {
        jwtKey = Base64.getEncoder().encodeToString(jwtKey.getBytes());
    }

    public String createToken(String login){
        Date now = new Date();
        Date expiration = new Date(now.getTime()+ 1600000);
        Algorithm algorithm = Algorithm.HMAC512(jwtKey);

        return JWT.create()
                .withIssuer(login)
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .sign(algorithm);
    }

    public Authentication validateToken(String token){
        Algorithm algorithm = Algorithm.HMAC512(jwtKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        ArchiveUser user = authenticationService.findByLogin(decodedJWT.getIssuer());

        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }

    public Authentication validateCredential(ArchiveUser request){
        ArchiveUser user = authenticationService.authenticate(request);

        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }
}
