package com.example.springsecuritystudy.config.security;

import com.example.springsecuritystudy.auth.dto.PrincipalDetail;
import com.example.springsecuritystudy.auth.provider.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;
  @Override
  protected void doFilterInternal(HttpServletRequest request , HttpServletResponse response ,
      FilterChain chain) throws IOException, ServletException {
    final String token = jwtTokenProvider.extractToken(request);
    System.out.println("token = " + token);
    if(!jwtTokenProvider.validateToken(token)){
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      // 응답 바디 데이터 구성
      Map <String, Object> body = new HashMap <>();
      body.put("code", "401 Unauthorized");
      body.put("error", "만료된 토큰");

      // JSON으로 변환하여 응답 스트림에 작성
      new ObjectMapper().writeValue(response.getOutputStream(), body);

      // 다음 필터를 타지 않음
      return;
    }

    final PrincipalDetail principalDetail = jwtTokenProvider.verify(token);
    final Authentication authentication = new UsernamePasswordAuthenticationToken(
        principalDetail, null, principalDetail.getAuthorities()
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(request, response);
  }
}
