package com.cos.blog.config.auth;


import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipal;
//로그인을 처리해주는 서비스 클래스
@Service
public class PrincipalDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    //스프링이 로그인 요청을 가로챌때 username,password 벼수 2개를 가로채는데
    // password 부분 처리는 알아서 함.
    // 따라서 우리가 해당 username이 있는지만 확인해주기만 하면 됨.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User principal = userRepository.findByUsername(username)
                .orElseThrow(()->{
                    return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. : "+username);
                });
        return new PricipalDetail(principal);//return을 해줄때 시큐리티 세션저장소에 유저 정보가 저장이 된다.
    }
}
