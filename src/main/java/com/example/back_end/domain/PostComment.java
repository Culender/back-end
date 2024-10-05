package com.example.back_end.domain;

import com.example.back_end.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "POST_COMMENT")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostComment extends BaseEntity {

    @Id
    @Column(name="comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(name="content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post; //해당 게시글

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // 작성자

    //댓글 수정
    public void update(String content) {
        this.content = content;
    }


    public static PostComment toEntity(String content, User user, Post post) {
        return PostComment.builder()
                .content(content)
                .user(user)
                .post(post)
                .build();
    }
}
