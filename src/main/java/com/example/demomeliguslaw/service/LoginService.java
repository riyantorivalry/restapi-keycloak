package com.example.demomeliguslaw.service;

import org.keycloak.representations.AccessTokenResponse;

import com.example.demomeliguslaw.dto.LoginRequestDto;


public interface LoginService {
	//	public LoginResponseDTO checkLogin(String hashcode, LoginRequestDto loginDTO) throws Exception;
	//
	//	public UserDTO getUserInfo(String token) throws Exception;

	public AccessTokenResponse getToken (LoginRequestDto login) throws Exception;

	public AccessTokenResponse getRefreshToken (String refreshToken) throws Exception;
}
