package com.example.springsecuritystudy.auth.dto;

import com.example.springsecuritystudy.auth.type.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

  private String email;
  private String password;
  private Role role;
}
