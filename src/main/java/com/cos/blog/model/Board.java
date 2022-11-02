package com.cos.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob //대용량 데이터
    private String content;

    private int count;//조회수

    @ManyToOne(fetch = FetchType.EAGER)// Many=Board, One=User 한명의 유저는 여러개의 게시글을 작성할 수 있다.
    @JoinColumn(name="userId")//테이블 컬럼명은 userId로 만들어짐
    private User user;

                            //Reply테이블의 필드이름을 적어주면 된다! | cascade = CascadeType.REMOVE -> board게시물을 지울때 reply댓글도 모두 지운다는 뜻
    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)//mapppedBy 연관관계 주인이 아니다( 난 FK가 아니에요! )DB에 컬럼을 만들지 마세요.
    //join컬럼은 필요없음! 왜냐 하나의 게시글에 여러개의 reply가 달릴 수 있기에 데이터 1,5,6 이렇게 넣을거 아니잖아!
    @JsonIgnoreProperties({"board"})//무한참조 방지 -> 다이렉트로 reply에 접근하면 reply객체안의 board객체를 주지만 board를 통해서 접근하면 reply안의 board객체를 주지 않음!
    @OrderBy("id desc")
    private List<Reply> replys;

    @CreationTimestamp
    private Timestamp createDate;

}
