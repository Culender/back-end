package com.example.back_end.post.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentDto {
    private String content;  // 수정할 댓글 내용
}
