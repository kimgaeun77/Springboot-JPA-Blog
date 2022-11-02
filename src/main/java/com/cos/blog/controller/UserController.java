package com.cos.blog.controller;

import com.cos.blog.model.KakaoProfile;
import com.cos.blog.model.OAuthToken;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
// 인증이 안된 사용자들이 출입할 수 있는 경로를 /auth/** 허용
// 그냥 주소가 /  이면 index.jsp허용
// static 이하에 있는 /js/**, /css/**, /image/** 허용

@Controller
public class UserController {

    @Value("${cos.key}")
    private String cosKey;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @GetMapping("/auth/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @GetMapping("/auth/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }


    @GetMapping("/auth/kakao/callback")//인가 코드를 받는 주소
    public String kakaoCallback(String code) {

        RestTemplate rt = new RestTemplate();

        //HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "ba2aa1db098ae55aec6349ea28d6b815");
        params.add("redirect_uri", "http://localhost:8000/auth/kakao/callback");
        params.add("code", code);

        //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        //Http 요청하기 - Post방식으로 - 그리고 response 변수에 응답 받음.
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class//응답 받을 type
        );

        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oauthToken;

        try {
            //json문자열을 java object로 변환
            oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println("카카오 엑세스 토큰 : " + oauthToken.getAccess_token());


//      ------------- 여기서부터는 발급 받은 access토큰을 통해 회원 정보 조회 하는 단계 -------------

        // process2 발급 받은 토큰을 통해 회원 정보 조회
        RestTemplate rt2 = new RestTemplate();

        //HttpHeader 오브젝트 생성
        HttpHeaders header2 = new HttpHeaders();
        header2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
        header2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //아래의 exchange이 함수 안에는 HttpEntity 오브젝트만 들어갈 수 있어서 HttpEntity 오브젝트로 header정보를 감싸는 것
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 =
                new HttpEntity<>(header2);

        //Http 요청하기 - Post방식으로 - 그리고 response2 변수에 응답 받음.
        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest2,
                String.class
        );
        System.out.println(response2.getBody());

        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile;

        try {
            kakaoProfile = objectMapper.readValue(response2.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
//        ------------- (발급 받은 access 토큰으로) 사용자의 정보에 접근 후 회원가입 시키는 로직 -------------

        //System.out.println("카카오 아이디(번호) :" + kakaoProfile.getId());
        //System.out.println("카카오 이메일 :" + kakaoProfile.getKakao_account().getEmail());

        System.out.println("블로그서버 유저네임 :" + kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId());
        System.out.println("블로그서버 이메일 :" + kakaoProfile.getKakao_account().getEmail());

        //UUID garbagePassword = UUID.randomUUID();
        System.out.println("블로그서버 패스워드 :" + cosKey);

        //카카오계정을 기반으로 회원가입할때 필요한 id, pw, email 생성
        User kakaoUser = User.builder()
                .username(kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId())
                .password(cosKey)
                .email(kakaoProfile.getKakao_account().getEmail())
                .oauth("kakao")//카카오 로그인 사용자일경우 회원수정을 못하게 하기 위해서 카카오 로그인 사용자인지 확인할 수 있는 필드
                .build();

        //가입자인지 비가입자인지 체크해서 처리 해야함.(어차피 카카오 로그인할때는 위에서 만든 정보들을 바탕으로 회원이 아니라면 회원가입을 시킴!)
        User originUser = userService.회원찾기(kakaoUser.getUsername());

        System.out.println("회원가입 전 user 객체" + kakaoUser);
        if (originUser.getUsername() == null) {//회원정보가 없는거임!( = 즉 회원가입이 안되어있다는 것)
            System.out.println("기존 회원이 아니기에 자동 회원가입을 진행합니다.");
            userService.회원가입(kakaoUser);
        }

        System.out.println("회원가입 후 user 객체" + kakaoUser);

        //로그인 처리
        System.out.println("자동으로 로그인을 시작합니다.");
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                kakaoUser.getUsername(),
                                cosKey
                        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/";
    }

    @GetMapping("/user/updateForm")
    public String updateForm() {
        return "user/updateForm";
    }
}
