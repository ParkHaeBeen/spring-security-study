package com.example.springsecuritystudy.error.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Error{
  // User
  USER_ALREADY_LEAVE(HttpStatus.BAD_REQUEST, "이미 탈퇴한 회원입니다."),
  EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "등록되지 않은 이메일입니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
  EMAIL_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
  EMAIL_CODE_MIS_MATCH(HttpStatus.BAD_REQUEST, "이메일 인증 코드가 일치하지 않습니다."),
  USER_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "현재 비밀번호를 확인해주세요.");

  private final HttpStatus httpStatus;
  private final String errorMessage;
}
