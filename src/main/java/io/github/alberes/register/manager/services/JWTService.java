package io.github.alberes.register.manager.services;

import io.github.alberes.register.manager.constants.MessageConstants;
import io.github.alberes.register.manager.controllers.dto.TokenDto;
import io.github.alberes.register.manager.domains.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    private String secretKey;

    @Value("${app.session.expirationtime}")
    private int sessionExpiration;

    @Autowired
    private DateTimeFormatter formatter;

    public JWTService(){
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(MessageConstants.HMACSHA256);
            SecretKey sk = keyGenerator.generateKey();
            this.secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public TokenDto generateToken(UserPrincipal userPrincipal) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put(MessageConstants.ID, userPrincipal.getUserAccount().getId());
        claims.put(MessageConstants.NAME, userPrincipal.getUserAccount().getName());
        claims.put(MessageConstants.EMAIL, userPrincipal.getUserAccount().getEmail());
        claims.put(MessageConstants.PROFILES, userPrincipal.getUserAccount().getRoles());
        claims.put(MessageConstants.REGISTRATION_DATE, userPrincipal.getUserAccount().getCreatedDate().format(this.formatter));
        Date startDate = new Date(System.currentTimeMillis());
        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(this.sessionExpiration);
        Date expiration = Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant());
        return new TokenDto(Jwts.builder()
                .claims()
                .add(claims)
                .subject(userPrincipal.getUserAccount().getEmail())
                .issuedAt(startDate)
                .expiration(expiration)
                .and()
                .signWith(getKey())
                .compact(), expiration.getTime());
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}