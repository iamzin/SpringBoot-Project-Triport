package com.project.triport.kakao;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoOAuth2 {
    public KakaoUserInfo getUserInfo(String authorizedCode) {
        // 1. 인가 코드 -> access token
        String accessToken = getAccessToken(authorizedCode);
        // 2. access token -> Kako User 정보
        KakaoUserInfo userInfo = getUserInfoByToken(accessToken);

        return userInfo;
    }

    private String getAccessToken(String authorizedCode) {
        // HttpHeader Object 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody Object 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
//        params.add("client_id", "${kakao.rest-api.key}");
        params.add("client_id", "b30e166ade03d146889e1b012679fcf6");
//        params.add("redirect_uri", "http://localhost:8080/auth/kakao/callback");
        params.add("redirect_uri", "https://eungenie.me/auth/kakao/callback");
        params.add("code", authorizedCode);

        // HttpHeader와 HttpBody를 하나의 Object에 담기
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // POST로 Http 요청해서 변수 응답 받기
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // JSON -> access token 파싱
        String tokenJson = response.getBody();
        JSONObject rjson = new JSONObject(tokenJson);
        String accessToken = rjson.getString("access_token");

        return accessToken;
    }

    private KakaoUserInfo getUserInfoByToken(String accessToken) {
        // HttpHeader Object 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpHeader와 HttpBody를 하나의 Object에 담기
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        // POST로 Http 요청해서 변수 응답 받기
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        JSONObject body = new JSONObject(response.getBody());
        Long id = body.getLong("id");
        // TODO: 이메일 제공 동의하지 않을 경우 랜덤 값 적용 (nullable = false니까)
        String email = body.getJSONObject("kakao_account").getString("email");
        String nickname = body.getJSONObject("properties").getString("nickname");
//        String profileImgUrl = body.getJSONObject("properties").getString("profile_image");
        String profileImgUrl = "https://i.ibb.co/MDKhN7F/kakao-11.jpg";

        return new KakaoUserInfo(id, email, nickname, profileImgUrl);
    }
}
