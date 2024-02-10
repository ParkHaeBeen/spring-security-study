package com.example.springsecuritystudy.auth.dto;

import com.example.springsecuritystudy.auth.domain.Member;
import com.example.springsecuritystudy.auth.type.MemberStatus;
import com.example.springsecuritystudy.auth.type.Role;
import java.util.ArrayList;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Builder
@AllArgsConstructor
public class PrincipalDetail implements UserDetails {

  private String email;
  private String password;
  private Role role;
  private MemberStatus status;

  public PrincipalDetail(String email , Role role) {
    this.email = email;
    this.role = role;
  }

  public static PrincipalDetail from(final Member member) {
    return PrincipalDetail.builder()
        .email(member.getEmail())
        .password(member.getPassword())
        .status(member.getStatus())
        .role(member.getRole())
        .build();
  }
  @Override
  public Collection <? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList <>();
    authorities.add(new SimpleGrantedAuthority(role.name()));
    return authorities;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public String getPassword() {
    return this.password;
  }
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return this.status != MemberStatus.DRAWN;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return this.status == MemberStatus.ACTIVE;
  }
}
