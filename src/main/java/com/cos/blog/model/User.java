package com.cos.blog.model;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.management.relation.Role;
import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@DynamicInsert //insert시에 null인 필드를 제외시켜준다.
@Entity//이 어노테이션을 통해서 User클래스를 기반으로 mysql에 테이블이 생성된다.
public class User {
    
    @Id//pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)//프로젝트에 연결된 DB의 넘버링 전략을 따라감(ex 오라클이면 sequence)
    private int id;//시퀀스
                                            //중복제거
    @Column(nullable = false, length = 100, unique = true)
    private String username;//아이디
    
    @Column(nullable = false, length = 100)
    private String password;
    
    @Column(nullable = false, length = 50)
    private String email;

    //@ColumnDefault("user")default값 설정
    //RoleType은 enum이다. enum을 사용하면 RoleType에 정의 되어있는 값들만 들어갈 수 있다.
    @Enumerated(EnumType.STRING)//DB에는 RoleType이라는 데이터 타입이 없으니 해당 enum이 문자열이라는것을 명시해줘야함
    private RoleType role;//ADMIN, USER

    //카카오 로그인인지 확인하는 컬럼(카카오 로그인은 비밀번호를 바꾸면 안된다!)
    //비밀번호를 바꾸면 처음 회원가입할때의 비밀번호와 일치하지 않아 로그인 불가능하다.(카카오 로그인 api에서 특정 비밀번호로 설정해주고 있다)
    private String oauth;//nullable 기본값 -> true
    
    @CreationTimestamp//시간이 자동으로 입력된다.
    private Timestamp createDate;//가입일
    
}
