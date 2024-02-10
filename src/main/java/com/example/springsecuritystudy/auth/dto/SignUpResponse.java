package com.example.springsecuritystudy.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpResponse {
  private String email;
}
