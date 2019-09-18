package be.wouterversyck.shoppinglistapi.security.services;

import be.wouterversyck.shoppinglistapi.security.utils.SecurityConstants;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.Test;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class JwtServiceTest {

    private String jwtSecretKey = "dddddddddddddddddddfffffffffffffffffffffffcccccccccccccccccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee";
    private JwtService jwtService = new JwtService(jwtSecretKey);

    private static final String USERNAME = "USERNAME";

    @Test(expected = MalformedJwtException.class)
    public void shouldThrowMalformedJwtException_WhenInvalidTokenIsProvided() {
        jwtService.parseToken("Bearer token");
    }

    @Test(expected = MalformedJwtException.class)
    public void shouldThrowMalformedJwtException_WhenNoTokenIsProvided() {
        jwtService.parseToken("");
    }

    @Test(expected = MalformedJwtException.class)
    public void shouldThrowMalformedJwtException_WhenTokenDoesNotStartWithPrefix() {
        jwtService.parseToken(generateRawToken());
    }

    @Test(expected = ExpiredJwtException.class)
    public void shouldThrowExpiredJwtException_WhenTokenIsExpired() {
        String token = generateExpiredToken();
        jwtService.parseToken(token);
    }

    @Test
    public void shouldCreateCorrectUsernamePasswordAuthenticationToken_WhenTokenStringIsPassed() {
        String token = generateValidToken();

        var authenticationToken = jwtService.parseToken(token);

        assertThat(authenticationToken.getPrincipal()).isEqualTo(USERNAME);
    }

    @Test
    public void shouldGenerateCorrectTokenString_WhenUserIsPassed() {
        String token = generateRawToken();

        var parsedToken = Jwts.parser()
                .setSigningKey(jwtSecretKey.getBytes())
                .parseClaimsJws(token);

        assertThat(parsedToken.getBody().getSubject()).isEqualTo(USERNAME);
    }

    private String generateRawToken() {
        return generateToken(new Date(System.currentTimeMillis() + 864000000));
    }

    private String generateValidToken() {
        String token = generateToken(new Date(System.currentTimeMillis() + 864000000));

        return format("Bearer %s", token);
    }

    private String generateExpiredToken() {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MONTH, -1);
        String token = generateToken(date.getTime());

        return format("Bearer %s", token);
    }

    private String generateToken(Date expirationDate) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(jwtSecretKey.getBytes()), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setSubject(USERNAME)
                .setExpiration(expirationDate)
                .claim("roles", Collections.emptyList())
                .compact();
    }
}
