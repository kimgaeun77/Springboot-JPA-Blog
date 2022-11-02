package com.cos.blog.service;

import com.cos.blog.config.auth.PricipalDetail;
import com.cos.blog.dto.ReplySaveReqeustDto;
import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.repository.ReplyRepository;
import com.cos.blog.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
//@RequiredArgsConstructor -> 2. 이 어노테이션을 붙여줌으로써 BoardService객체를 생성할때 생성자의 인자를 통해 필요한 객체를 주입받아 초기화 시켜줄 수 있다.
public class BoardService {

    private final BoardRepository boardRepository;//1. final이 붙어있는 레퍼런스 변수는 선언과 동시에 초기화가 되어야한다. 하지만 아직 초기화가 되지 않은상태
    private final ReplyRepository replyRepository;

    public BoardService(BoardRepository boardRepository, ReplyRepository replyRepository) {
        this.boardRepository = boardRepository;
        this.replyRepository = replyRepository;
    }

    @Transactional
    public void 글쓰기(Board board, User user) {
        board.setCount(0);
        board.setUser(user);//게시글을 작성할때 어떤 user가 썼는지 넣어줘야 함!
        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public Page<Board> 글목록(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Board 글상세보기(int id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> {
                    return new IllegalArgumentException("글 상세보기 실패 : 아이디를 찾을 수 없습니다.");
                });
    }

    @Transactional
    public void 글삭제하기(int id) {
        boardRepository.deleteById(id);
    }

    @Transactional
    public void 글수정하기(int id, Board requestBoard) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> {
                    return new IllegalArgumentException("글 찾기 실패 : 아이디를 찾을 수 없습니다.");
                });//영속화 완료
        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());
        //해당 메소드 종료시(service종료시)트랜잭션이 종료된다.
        //이때 더티체킹이 일어나 jpa 영속성 컨텍스트에 담긴 객체랑 현재 object의 사이간의 변경이 감지되면 자동 업데이트가 된다! -> flush
    }

    /*
    @Transactional
    public void 댓글쓰기(User user, int boardId, Reply requestReply) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> {
            return new IllegalArgumentException("댓글 작성 실패 : 게시글 아이디를 찾을 수 없습니다.");
        });
        requestReply.setUser(user);
        requestReply.setBoard(board);
        replyRepository.save(requestReply);

    }*/

    //방법2
    @Transactional
    public void 댓글쓰기(ReplySaveReqeustDto replySaveReqeustDto) {

        int result = replyRepository.mSave(replySaveReqeustDto.getUserId(), replySaveReqeustDto.getBoardId(), replySaveReqeustDto.getContent());
        System.out.println("boardService : "+result);
    }

    @Transactional
    public void 댓글삭제(int replyId) {
        replyRepository.deleteById(replyId);
    }
}

