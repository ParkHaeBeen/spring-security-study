package com.example.springsecuritystudy.auth.controller;

import com.example.springsecuritystudy.auth.dto.PrincipalDetail;
import com.example.springsecuritystudy.auth.dto.SignInRequest;
import com.example.springsecuritystudy.auth.dto.SignInResponse;
import com.example.springsecuritystudy.auth.dto.SignUpRequest;
import com.example.springsecuritystudy.auth.dto.SignUpResponse;
import com.example.springsecuritystudy.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  @PostMapping
  public ResponseEntity<SignUpResponse> signUp(@RequestBody final SignUpRequest request){
    return ResponseEntity.ok(authService.signup(request));
  }

  @PostMapping("/login")
  public ResponseEntity<SignInResponse> signIn(@RequestBody final SignInRequest request){
    return ResponseEntity.ok(authService.signIn(request));
  }

  @GetMapping
  @PreAuthorize("permitAll()")
  public ResponseEntity<?> checkSecurity(@AuthenticationPrincipal PrincipalDetail detail){
    return ResponseEntity.ok(detail);
  }

  @GetMapping("/test")
  @PreAuthorize("permitAll()")
  public ResponseEntity<?> checkNoSecurity(){
    return ResponseEntity.ok().build();
  }
}
