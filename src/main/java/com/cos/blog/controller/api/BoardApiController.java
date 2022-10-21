package com.cos.blog.controller.api;

import com.cos.blog.config.auth.PricipalDetail;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.Board;
import com.cos.blog.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoardApiController {

    @Autowired
    private BoardService boardService;

    @PostMapping("/api/board")                              //로그인한 사용자 정보를 파라메터로 받고싶을때 사용하는 어노테이션
    public ResponseDto<Integer> save(@RequestBody Board board, @AuthenticationPrincipal PricipalDetail principal){
        boardService.글쓰기(board, principal.getUser());
        //System.out.println(principal.getUser());/로그인할때 username으로 DB에서 모든 정보를 꺼내온 user객체임!
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
    }

    @DeleteMapping("/api/board/{id}")
    public ResponseDto<Integer> deleteById(@PathVariable int id){
        boardService.글삭제하기(id);
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
    }

    @PutMapping("/api/board/{id}")
    public ResponseDto<Integer> update(@RequestBody Board board, @PathVariable int id){
        boardService.글수정하기(id, board);
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
    }
}
