package com.example.back_end.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPostDto {

    private Long postId;
    private String category; //주제
    private String title; //제목
    private String nickname; //작성자
    private String content; //본문 내용
    private String image; //첨부 이미지
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt; // 작성일자
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt; // 수정일자
    private Long likeCount; //좋아요 수
    private Long commentCount; //댓글 수
    private Boolean isLiked; //좋아요 여부
}
