package com.cos.blog.service;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired//비밀번호를 암호화(해시) 해줌!
    private BCryptPasswordEncoder encoder;

    @Transactional
    public void 회원가입(User user) {
        String rowPassword = user.getPassword();//원문
        String encPassword = encoder.encode(rowPassword);//해시
        user.setPassword(encPassword);
        user.setRole(RoleType.USER);

        userRepository.save(user);
    }

    @Transactional
    public void 회원수정(User user) {
        //영속화를 하면 좋은점은 영속화된 오브젝트를 변경하면 자동으로 DB에 update문을 날려줌!
        User persistance = userRepository.findById(user.getId())
                .orElseThrow(() -> {
                    return new IllegalArgumentException("회원 찾기 실패");
                });
        //비밀번호 해시(비밀번호를 암호화해서 넣어줘야함)
        String rowPassword = user.getPassword();
        String encPassword = encoder.encode(rowPassword);
        persistance.setPassword(encPassword);
        persistance.setEmail(user.getEmail());

    }
}
