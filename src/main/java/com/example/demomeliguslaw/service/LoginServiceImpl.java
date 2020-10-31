package com.example.demomeliguslaw.service;

import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demomeliguslaw.dto.LoginRequestDto;
import com.example.demomeliguslaw.util.UserUtil;


@Service
public class LoginServiceImpl implements LoginService{
	@Autowired
	private UserUtil userUtil;

	//	@Override
	//	public LoginResponseDTO checkLogin(String hashcode, LoginRequestDto loginDTO) throws Exception{
	//		User user = DaoServices.queryForObject(hashcode, "getUserByUserName", DaoServices.constructParam(loginDTO.getUsername()), User.class);
	//		if (user == null) {
	//			throw new Exception("Not Found");
	//		}
	//		//		logger.info("user result : {}", ClaimUtil.printObjectJson(user));
	//		LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
	//		BeanUtils.copyProperties(user, loginResponseDTO);
	//		return loginResponseDTO;
	//	}
	//
	//	@Override
	//	public UserDTO getUserInfo(String token) throws Exception {
	//		UserDTO userInfo = userUtil.getUserInfo(token);
	//
	//		return userInfo;
	//	}

	@Override
	public AccessTokenResponse getToken(LoginRequestDto login) throws Exception {
		AccessTokenResponse response = userUtil.getToken(login);

		return response;
	}

	@Override
	public AccessTokenResponse getRefreshToken(String refreshToken) throws Exception {
		AccessTokenResponse response = userUtil.getRefreshToken(refreshToken);

		return response;
	}
}
