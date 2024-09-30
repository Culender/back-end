package com.example.back_end.domain;

import com.example.back_end.BackEndApplication;
import com.example.back_end.post.dto.CreatePostDto;
import com.example.back_end.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "POSTS")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long post_id;

    @Column(name="date")
    private String date;

    @Column(name="place")
    private String place;

    @Column(name="content")
    private String content;

    @Column(name="image")
    private String image;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    private User user;

    public static Post toEntity(CreatePostDto createPostDto, User user, String imgPath) {
        return Post.builder()
                .user(user)
                .date(createPostDto.getDate())
                .place(createPostDto.getPlace())
                .content(createPostDto.getContent())
                .image(imgPath)
                .build();
    }
}
