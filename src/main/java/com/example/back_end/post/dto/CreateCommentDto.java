package com.example.back_end.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentDto {

    @NotNull(message = "Id가 존재해야 해당 글의 댓글작성이 가능합니다.")
    private Long postId;  // 게시글의 ID
    @NotBlank(message = "내용은 필수 입력값입니다.")
    private String content; //댓글 내용
}
