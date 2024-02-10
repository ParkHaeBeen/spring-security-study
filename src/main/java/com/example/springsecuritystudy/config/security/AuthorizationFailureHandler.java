package com.example.springsecuritystudy.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

@Slf4j
public class AuthorizationFailureHandler implements AccessDeniedHandler {
  @Override
  public void handle(HttpServletRequest request , HttpServletResponse response ,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    log.error("권한 오류");
    response.sendError(HttpServletResponse.SC_FORBIDDEN);
  }
}
