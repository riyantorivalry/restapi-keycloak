package com.example.demomeliguslaw.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class SecurityException extends RuntimeException{
	public SecurityException (String msg) {
		super(msg);
	}
}
