package com.cos.blog.repository;

import com.cos.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

    //JPA Naming 쿼리
    //메소드 이름을 보고 자동으로 쿼리가 생성된다. => SELECT * FROM user WHERE username = ? AND password = ?
    User findByUsernameAndPassword(String username, String password);

    /*
    @Query(value="SELECT * FROM user WHERE username = ? AND password = ?", nativeQuery = true)
    User login(String username, String password);*/


}
