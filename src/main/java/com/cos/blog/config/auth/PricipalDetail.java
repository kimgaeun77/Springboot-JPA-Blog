package com.cos.blog.config.auth;

import com.cos.blog.model.User;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
@Getter
public class PricipalDetail implements UserDetails {

    private User user;

    public PricipalDetail(User user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    //계정이 만료되지 않았는지 리턴한다. (true: 만료안됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠겨있지 않았는지 리턴한다. (true: 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호가 만료되지 않았는지 리턴한다.(true: 만료안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 활성화(사용가능)인지 리턴한다.(true : 활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }

    //계정의 권한을 return 해줌
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        //GrantedAuthority type의 객체만 list에 담길 수 있음
        Collection<GrantedAuthority> collectors = new ArrayList<>();//arraylist도 collection의 하위계층임!
        collectors.add(()->{return "ROLE_"+user.getRole();});
        return collectors;
    }
}
