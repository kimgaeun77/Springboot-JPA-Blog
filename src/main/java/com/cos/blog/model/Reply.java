package com.cos.blog.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 200)
    private String content;

    @ManyToOne
    @JoinColumn(name="boardId")
    private Board board;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;//답글을 누가 달았는지

    @CreationTimestamp
    private Timestamp createDate;


    @Override
    public String toString() {//오브젝트를 출력하면 자동으로 toString 함수가 실행된다.
        return "Reply [id=" + id + ", content=" + content + ", board=" + board + ", user=" + user + ", createDate="
                + createDate + "]";
    }
}
