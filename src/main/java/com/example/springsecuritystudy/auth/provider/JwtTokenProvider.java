package com.example.springsecuritystudy.auth.provider;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.impl.ClaimsHolder;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.springsecuritystudy.auth.domain.Member;
import com.example.springsecuritystudy.auth.dto.PrincipalDetail;
import com.example.springsecuritystudy.auth.dto.TokenResponse;
import com.example.springsecuritystudy.auth.type.Role;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
  private final String TOKEN_HEADER = "Authorization";
  private final String TOKEN_PREFIX = "Bearer ";

  @Value("${spring.security.accesstoken.expired}")
  private long ACCESS_TOKEN_VALID_TIME;

  @Value("${spring.security.refreshtoken.expired}")
  private long REFRESH_TOKEN_VALID_TIME;

  @Value("${spring.security.jwt.secret}")
  private String SECRET_KEY;

  public TokenResponse createToken(final Member member){
    final Date now = new Date();
    final long accessTokenExpiredDate = now.getTime() + ACCESS_TOKEN_VALID_TIME;
    final long refreshTokenExpiredDate = now.getTime() + REFRESH_TOKEN_VALID_TIME;

    final String email = member.getEmail();
    final Role role = member.getRole();

    final String accessToken = JWT.create()
        .withSubject(email)
        .withClaim("role", role.name())
        .withExpiresAt(new Date(accessTokenExpiredDate))
        .withIssuedAt(now)
        .sign(Algorithm.HMAC512(SECRET_KEY));

    final String refreshToken = JWT.create()
        .withSubject(email)
        .withClaim("role", role.name())
        .withExpiresAt(new Date(refreshTokenExpiredDate))
        .withIssuedAt(now)
        .sign(Algorithm.HMAC512(SECRET_KEY));

    return TokenResponse.builder()
        .grantType(TOKEN_PREFIX.trim())
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .accessTokenExpiredDate(LocalDateTime.now().plusSeconds(accessTokenExpiredDate))
        .build();
  }

  public boolean validateToken(final String token){
    if(!StringUtils.hasText(token)) {
      return false;
    }
    return JWT.decode(token).getExpiresAt().after(new Date(System.currentTimeMillis()));
  }

  public PrincipalDetail verify(final String token){
    log.debug("start verify token={}", token);

    DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(token);

    String email = decodedJWT.getSubject();
    String role = decodedJWT.getClaim("role").asString();

    return new PrincipalDetail(email,Role.valueOf(role));
  }

  public String extractToken(final HttpServletRequest request){
    String tokenHeader = request.getHeader(TOKEN_HEADER);

    if (!StringUtils.hasLength(tokenHeader) || !tokenHeader.startsWith(TOKEN_PREFIX)) {
      return null;
    }

    return tokenHeader.substring(TOKEN_PREFIX.length());
  }
}
