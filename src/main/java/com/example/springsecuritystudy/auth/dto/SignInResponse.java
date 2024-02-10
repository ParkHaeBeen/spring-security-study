package com.example.springsecuritystudy.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignInResponse {

  private TokenResponse token;
  private String email;
}
