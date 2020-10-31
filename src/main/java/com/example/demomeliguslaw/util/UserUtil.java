package com.example.demomeliguslaw.util;

import java.util.List;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demomeliguslaw.dto.KeyCloakUserInfo;
import com.example.demomeliguslaw.dto.LoginRequestDto;
import com.example.demomeliguslaw.dto.UserDTO;
import com.example.demomeliguslaw.rest.RestTemplateApi;
@Component
public class UserUtil {
	@Autowired
	private RestTemplateApi restTemplateApi;

	@Value("${custom.keycloak.userinfo.url}")
	private String keyclaokUserInfoUrl;
	@Value("${custom.keycloak.refresh-token.url}")
	private String keyclaokRefreshTokenUrl;

	@Value("${keycloak.auth-server-url}")
	private String authServerUrl;
	@Value("${keycloak.realm}")
	private String realm;
	@Value("${keycloak.resource}")
	private String clientId;

	public  UserDTO getUserInfo(String token) throws Exception{
		UserDTO user = new UserDTO();
		KeyCloakUserInfo  keyCloakUserInfo = extractToken(token);
		//		logger.info("KeyCloakUserInfo == {} ", keyCloakUserInfo.getPreferred_username());
		user.setUsername(keyCloakUserInfo.getPreferred_username());
		//		String hashcode = Util.generateHashCode();
		List<String> roles = null;
		for (String roleClaim : roles) {
			//			logger.info("role DB : {}",roleClaim.getName());
			//			lsRoleDB.add(roleClaim.getName());
		}
		//override role jika di DB punnya role
		//		if(lsRoleDB != null && lsRoleDB.size() > 0)
		//			user.setRoles(lsRoleDB);

		//		if (user.getUsername() == null) {
		//			throw new BusinessException(ClaimCons.Response.ERROR_INVALID_TOKEN.getCode(), ClaimCons.Response.ERROR_INVALID_TOKEN.getMessage());
		//		}

		return user;
	}

	public AccessTokenResponse getToken(LoginRequestDto login) throws Exception {
		// TODO Auto-generated method stub
		Keycloak keycloak = null;

		try {
			keycloak = Keycloak.getInstance(
					authServerUrl,
					realm,
					login.getUsername(),
					login.getPassword(),
					clientId);
		}catch (Exception e) {
			//			logger.error("Error retrieving Keycloak Token : ", e.getMessage());
			throw new Exception(e);
		}
		AccessTokenResponse response = new AccessTokenResponse();
		BeanUtils.copyProperties(keycloak.tokenManager().getAccessToken(), response);

		return response;
	}

	public AccessTokenResponse getRefreshToken(String refreshToken) throws Exception {
		try {
			String customUrl = "/realms/"+realm+keyclaokRefreshTokenUrl;
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(authServerUrl + customUrl);
			MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
			requestBody.add("grant_type", "refresh_token");
			requestBody.add("refresh_token", refreshToken);
			requestBody.add("client_id",clientId);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);
			ResponseEntity<AccessTokenResponse> response = restTemplateApi.restTemplate(builder.toUriString(), HttpMethod.POST, request, AccessTokenResponse.class);
			return response.getBody();
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception(e);		}
	}

	public KeyCloakUserInfo  extractToken(String token) throws Exception{
		try {
			String customUrl = "/realms/"+realm+keyclaokUserInfoUrl;
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(authServerUrl + customUrl);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<?> request = new HttpEntity<>(headers);
			ResponseEntity<KeyCloakUserInfo> response = restTemplateApi.restTemplate(builder.toUriString(), HttpMethod.GET, request, KeyCloakUserInfo.class);

			return response.getBody();
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception(e);
		}
	}
}
