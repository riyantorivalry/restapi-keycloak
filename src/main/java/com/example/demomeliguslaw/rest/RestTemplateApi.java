package com.example.demomeliguslaw.rest;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class RestTemplateApi {
	@Autowired
	private RestTemplateBuilder restTemplateBuilder;
	@Value("${keycloak.auth-server-url}")
	private String keycloakBaseUrl;

	@Value("${rest.read.timeout}")
	private String readTimeOut;
	@Value("${rest.connection.timeout}")
	private String connectionTimeout;

	public <T> ResponseEntity<T> restTemplate(String url, HttpMethod httpMethod, HttpEntity<?> entity, Class<T> T) {
		//		logger.info("full url = " + url);
		Duration readTimeOutInMiliSecond = Duration.ofMillis(Integer.valueOf(readTimeOut));
		Duration writeTimeOutInMiliSecond = Duration.ofMillis(Integer.valueOf(connectionTimeout));
		RestTemplate restTemplate = restTemplateBuilder.setConnectTimeout(writeTimeOutInMiliSecond).setReadTimeout(readTimeOutInMiliSecond).build();
		ResponseEntity<T> response = restTemplate.exchange(url, httpMethod, entity, T);
		return response;
	}

	public <T> T keycloakGetTemplate(String customUrl,Map<String,Object> params, String headerToken, Class<T> T) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(keycloakBaseUrl + customUrl);
		if(params != null) {
			params.forEach((key,value) -> builder.queryParam(key, value));
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", headerToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<?> request = new HttpEntity<>(headers);
		ResponseEntity<T> response = restTemplate(builder.toUriString(), HttpMethod.GET, request, T);
		return response.getBody();
	}

	public <T> T keyCloakPostTemplate(String customUrl, Object reqDTO, Class<T> T) {
		//		logger.info("custom url : " + customUrl);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(keycloakBaseUrl + customUrl);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Object> request = null;
		if(reqDTO != null) {
			request = new HttpEntity<>(reqDTO, headers);
		} else {
			request = new HttpEntity<>(headers);
		}
		ResponseEntity<T> response = restTemplate(builder.toUriString(), HttpMethod.POST, request, T);
		return response.getBody();
	}

}
