package com.example.springsecuritystudy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordEncoderConfiguration {
  @Bean
  public org.springframework.security.crypto.password.PasswordEncoder bCryptPasswordEncoder(){
    return new BCryptPasswordEncoder();
  }
}
