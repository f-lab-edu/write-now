package kr.co.writenow.writenow.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.time.Duration;
import java.util.Date;
import java.util.function.Function;
import kr.co.writenow.writenow.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    public String generatedToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    private String makeToken(Date expiry, User user) {
        Date now = new Date();
        return Jwts.builder()
            .setHeaderParam("type", "jwt")
            .setIssuer(jwtProperties.getIssuer())
            .setIssuedAt(now)
            .setExpiration(expiry)
            .setSubject(user.getUserId())
            .claim("id", user.getUserId())
            .signWith(jwtProperties.getKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    public boolean isValidToken(String token, String userId) {
        String userIdByToken = getUserId(token);

        if (!userIdByToken.equals(userId)) {
            return false;
        }

        if (isTokenExpired(token)) {
            return false;
        }

        try {
            Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            log.warn(String.format("Invalid JWT signature. token: %s", token));
            throw new JwtException("JWT 토큰의 시그니처가 유효하지 않습니다..");
        } catch (MalformedJwtException e) {
            log.warn(String.format("Invalid JWT token. token: %s", token));
            throw new JwtException("유효하지 않은 JWT 토큰입니다.");
        } catch (ExpiredJwtException e) {
            log.warn(String.format("Expired JWT token. token: %s", token));
            throw new JwtException("기한이 만료된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token.");
            throw new JwtException(String.format("지원되지 않는 JWT 토큰입니다. token: %s", token));
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            throw new JwtException(
                String.format("JWT token compact of handler are invalid. token: %s", token));
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(jwtProperties.getKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public String getUserId(String token) {
        Claims claims = getClaims(token);
        return claims == null ? "" : claims.get("id", String.class);
    }

    private Date getExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = getClaims(token);
        return claimsTFunction.apply(claims);
    }
}
