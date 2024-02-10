package com.example.springsecuritystudy.config;

import com.example.springsecuritystudy.auth.provider.JwtTokenProvider;
import com.example.springsecuritystudy.auth.service.AuthService;
import com.example.springsecuritystudy.config.security.AuthorizationFailureHandler;
import com.example.springsecuritystudy.config.security.JwtAuthenticationEntryPoint;
import com.example.springsecuritystudy.config.security.JwtAuthorizationFilter;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtTokenProvider jwtTokenProvider;
  private final AuthService authService;
  private final PasswordEncoder passwordEncoder;

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer(){
    return web -> {
      web.ignoring()
          .requestMatchers(new AntPathRequestMatcher("/",HttpMethod.POST.name()))
          .requestMatchers(
              "/test","/h2-console/**", "/login"
              );
    };
  }

  @Bean
  public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(CorsConfigurer::disable)
        .sessionManagement(
           (sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .logout(Customizer.withDefaults());

    http.addFilterBefore(new JwtAuthorizationFilter(jwtTokenProvider), BasicAuthenticationFilter.class);

    http.exceptionHandling(exceptionHandler -> {
      exceptionHandler.authenticationEntryPoint(
          new JwtAuthenticationEntryPoint()
      );
      exceptionHandler.accessDeniedHandler(
          new AuthorizationFailureHandler()); // 인가(권한) 오류(403)
    });

    return http.build();
  }

  @Bean
  AuthenticationManager authenticationManager() {

    log.debug("[Bean 등록] AuthenticationManager");

    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(authService);
    provider.setPasswordEncoder(passwordEncoder);
    return new ProviderManager(provider);
  }
}
