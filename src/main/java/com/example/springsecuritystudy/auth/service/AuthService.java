package com.example.springsecuritystudy.auth.service;

import com.example.springsecuritystudy.auth.domain.Member;
import com.example.springsecuritystudy.auth.dto.PrincipalDetail;
import com.example.springsecuritystudy.auth.dto.SignInRequest;
import com.example.springsecuritystudy.auth.dto.SignInResponse;
import com.example.springsecuritystudy.auth.dto.SignUpRequest;
import com.example.springsecuritystudy.auth.dto.SignUpResponse;
import com.example.springsecuritystudy.auth.dto.TokenResponse;
import com.example.springsecuritystudy.auth.provider.JwtTokenProvider;
import com.example.springsecuritystudy.auth.repository.MemberRepository;
import com.example.springsecuritystudy.auth.type.MemberStatus;
import com.example.springsecuritystudy.error.ServiceException;
import com.example.springsecuritystudy.error.type.Error;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements UserDetailsService {

  private final JwtTokenProvider jwtTokenProvider;
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
    log.info("[+] loadUserByUsername start");
    Member member = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new ServiceException(Error.USER_NOT_FOUND));

    // 탈퇴한 회원 로그인 시도 시
    if (member.getStatus()!= MemberStatus.DRAWN) {
      throw new ServiceException(Error.USER_ALREADY_LEAVE);
    }

    log.debug(member.getEmail());
    return new PrincipalDetail(member.getEmail(), member.getRole());
  }

  public SignUpResponse signup(final SignUpRequest request){
    final Member member = Member.builder()
        .role(request.getRole())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .build();
    memberRepository.save(member);

    return SignUpResponse.builder()
        .email(request.getEmail())
        .build();
  }

  public SignInResponse signIn(final SignInRequest request){
    final Member member = memberRepository.findByEmail(request.getEmail()).get();
    final TokenResponse token = jwtTokenProvider.createToken(member);

    return SignInResponse.builder()
        .email(member.getEmail())
        .token(token)
        .build();
  }
}
