package com.cos.blog.config;

import com.cos.blog.config.auth.PrincipalDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration//빈등록
@EnableWebSecurity//시큐리티 필터가 등록이 된다.
@EnableGlobalMethodSecurity(prePostEnabled = true)//특정 주소로 접근하면 권한 및 인증을 미리 체크하겠다는 뜻 pre!
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalDetailService principalDetailService;

    @Bean
    public BCryptPasswordEncoder encodePWD(){//메소드의 리턴 객체가 스프링 빈 객체임을 선언함
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //시큐리티가 대신 로그인 해줄때 password를 가로채기 하는데
    //회원가입할때 DB에 넣은 password해시코드가 뭘로 해시가되어있는지 명시해줘야
    //(로그인할때)가로채기한 password를 동일한 해시로 암호화해서 DB에 들어가 있는 password와 동일한지 비교할 수 있다.


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()//csrf 토큰 비활성화(테스트시 걸어주는게 좋음)
                .authorizeRequests()//어떠한 요청이 들어오면
                .antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**", "/dummy/**")
                .permitAll()
                .anyRequest()//위에 요청을 제외한 다른 모든 요청들은
                .authenticated()//인증이 돼야 돼
                .and()
                .formLogin()
                .loginPage("/auth/loginForm")//인증이 필요한 페이지 요청이 온다면 /auth/loginForm으로 자동으로 이동이 된다.
                .loginProcessingUrl("/auth/loginProc")//스프링 시큐리티가 해당 주소로 요청 오는 로그인을 가로채서 대신 로그인을 해준다.
                .defaultSuccessUrl("/");//정상적으로 요청이 완료 되었을때 /로 이동

    }

    /*
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()//csrf 토큰 비활성화(테스트시 걸어주는게 좋음)
                .authorizeRequests()//어떠한 요청이 들어오면
                    .antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**")
                    .permitAll()
                    .anyRequest()//위에 요청을 제외한 다른 모든 요청들은
                    .authenticated()//인증이 돼야 돼
        .and()
                .formLogin()
                .loginPage("/auth/loginForm")//인증이 필요한 페이지 요청이 온다면 /auth/loginForm으로 자동으로 이동이 된다.
                .loginProcessingUrl("/auth/loginProc")//스프링 시큐리티가 해당 주소로 요청 오는 로그인을 가로채서 대신 로그인을 해준다.
                .defaultSuccessUrl("/");//정상적으로 요청이 완료 되었을때 /로 이동

        return http.build();
    }*/
}
