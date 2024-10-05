package com.example.back_end.domain;

import com.example.back_end.post.dto.CreatePostDto;
import com.example.back_end.record.dto.CreateRecordDto;
import com.example.back_end.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "POST")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long postId;

    @Column(name="category")
    private String category; //카테고리(주제)

    @Column(name="title")
    private String title; //게시글 제목

    @Column(name="content")
    private String content; //내용

    @Column(name="image")
    private String image; //첨부이미지

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    private User user; //작성자

    // 댓글 기능 (1:N 관계)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostComment> comments;

    // 좋아요 기능  (1:N 관계)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> likes;

    public static Post toEntity(CreatePostDto createPostDto, User user, String imgPath) {
        return Post.builder()
                .user(user)
                .category(createPostDto.getCategory())
                .title(createPostDto.getTitle())
                .content(createPostDto.getContent())
                .image(imgPath)
                .build();
    }


    public void updatePost(String title, String content, String imgPath) {
        this.title = title;
        this.content = content;
        this.image = imgPath;
    }
}
