package org.study.hydro.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Function;

/**
    * The class {@link JwtService} is responsible for the Java Web Token, thanks to which user authorization occurs.
    *
    * @author Aliaksandr Pishchala
 */
@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long expiration;

    public String extractUsername(String token) {
            return extractClaim(token, Claims::getSubject);
    }

    /**
        * The method generates a token.
        * @param userDetails class contains information about the user in security.
        *
        * @return the string value of the token.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * The method creates a token. It gets Map and UserDetails and packages the necessary information such as
     * extraClaims, user's name, Current date in milliseconds, token expiration date in milliseconds, secret key
     * and encryption algorithm into a string type.
     *
     * @param userDetails contains information about the user in security.
     * @param extraClaims the map for storing information.
     *
     * @return the string value of the token.
     */
    public String generateToken (
            Map<String, Object> extraClaims,
            UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(getExpiration())
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
        * The method returns the token expiration date.
        *
        * @return The date expiration.
     */
    private Date getExpiration() {
        return new Date(System.currentTimeMillis() + expiration);
    }

    /**
        * The method checks the validity of the token and the correspondence of the token to the user.
        *
        * @param token contains jwt string value.
        * @param userDetails contains information about the user in security.
        *
        * @return boolean value.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
       final String username = extractUsername(token);
       return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
        * The method checks that the token has not expired.
        *
        * @param token contains jwt string value.
        * @return boolean value check for expiration date.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * The method gets the validity date from the token.
     *
     * @param token contains jwt string value.
     * @return boolean value check for expiration date.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * The method pulls out Claims.
     *
     * @param token contains jwt string value.
     * @param claimResolver
     * @return extraClaims
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
            final Claims claims = extractAllClaims(token);
            return claimResolver.apply(claims);
    }

    /**
        * The method pulls out all Claims from the token.
        *
        * @param token contains jwt string value.
        * @return Claims.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder().setAllowedClockSkewSeconds(600000000)
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
        * The method obtains the secret key during encryption.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
