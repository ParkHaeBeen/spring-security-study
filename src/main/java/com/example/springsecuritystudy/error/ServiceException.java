package com.example.springsecuritystudy.error;


import com.example.springsecuritystudy.error.type.Error;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceException extends RuntimeException {

  HttpStatus statusCode;
  String errorMessage;

  public ServiceException(Error error) {
    this.statusCode = error.getHttpStatus();
    this.errorMessage = error.getErrorMessage();
  }
}
