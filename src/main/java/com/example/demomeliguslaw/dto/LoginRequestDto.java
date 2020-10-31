package com.example.demomeliguslaw.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
	private String username;
	private String password;

	public LoginRequestDto() {
		super();
	}
	public LoginRequestDto(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
}
