package com.example.back_end.post.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCommentDto {

    private Long commentId;
    private String nickname; //작성자
    private String content; //본문 내용
    private LocalDateTime createdAt; // 작성일자
    private LocalDateTime modifiedAt; // 수정일자
}
