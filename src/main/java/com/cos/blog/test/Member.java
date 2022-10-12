package com.cos.blog.test;

import lombok.*;

@Data // getter & setter
@NoArgsConstructor // 기본 생성자
//@AllArgsConstructor 모든 필드로 만들어진 생성자
public class Member {
    private int id;
    private String username;
    private String password;
    private String email;

    @Builder
    public Member(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
