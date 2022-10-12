package com.cos.blog.test;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Supplier;

@RestController
public class DummyController {

    @Autowired
    private UserRepository userRepository;

    //회원삭제
    @DeleteMapping("/dummy/user/{id}")
    public String delete(@PathVariable int id){

        try{
            userRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            return "삭제에 실패하였습니다. 해당 id는 DB에 없습니다.";
        }
        return "삭제되었습니다. id :" + id;
    }


    //정보 수정(비밀번호랑 이메일만)
    @Transactional//함수 종료시에 자동 commit이 된다. -> 더티체킹(영속성 컨텍스트에 들어가있는 객체와 save함수 매개변수에 넣은 객체가 서로 다르다면 변경을 감지해서 함수 종료후 update를 시켜준다
    @PutMapping("/dummy/user/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User requestUser){
        System.out.println("id : " + id);
        System.out.println("password: " + requestUser.getPassword());
        System.out.println("email : "+ requestUser.getEmail());
                                                //만약 못 찾으면
        User user = userRepository.findById(id).orElseThrow(()-> {
            return new IllegalArgumentException("수정에 실패하였습니다.");
        });
        user.setEmail(requestUser.getEmail());
        user.setPassword(requestUser.getPassword());

        //requestUser.setId(id);//PathVariable로 받은 데이터를 RequestUser객체에 넣어줌, id값을 넣어주면 시퀀스 작동x 넣어준 id값이 있다면(DB에)update문으로 바꿔줌!
        //requestUser.setUsername("ssar");

        //userRepository.save(user);

        return user;
    }

    //user테이블 전체목록 조회
    @GetMapping("/dummy/users")
    public List<User> list(){
        return userRepository.findAll();
    }

    //user테이블 목록 조회 + 페이징
    @GetMapping("/dummy/user")         //한 페이지당 데이터를 2건씩 들고오고, 정렬기준은 id이고 내림차순으로 정렬한다!(최신순으로)
    public List<User> pagingList(@PageableDefault(size = 2, sort="id", direction = Sort.Direction.DESC)Pageable pageable){
        Page<User> pagingUser = userRepository.findAll(pageable);

        List<User> users = pagingUser.getContent();
        return users;
    }

    // id에 해당하는 user 조회
    @GetMapping("/dummy/user/{id}")//매개변수이름을 URI에 있는 이름과 동일시해야함.
    public User detail(@PathVariable int id){
        User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
            @Override
            public IllegalArgumentException get() {
                return new IllegalArgumentException("해당 유저는 없습니다. id :" + id);
            }
        });
        return user;

    }
    //회원가입
    @PostMapping("/dummy/join")
    public String join(User user){

        System.out.println("username : " + user.getUsername());
        System.out.println("password : " + user.getPassword());
        System.out.println("email : " + user.getEmail());
        System.out.println("id : " + user.getId());
        System.out.println("role : " + user.getRole());
        System.out.println("createDate : " + user.getCreateDate());

        user.setRole(RoleType.USER);
        userRepository.save(user);
        return "회원가입이 완료되었습니다";
    }
}
