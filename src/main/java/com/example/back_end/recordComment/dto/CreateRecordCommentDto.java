package com.example.back_end.recordComment.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRecordCommentDto {
    @NotBlank(message = "관람기록의 기본키는 필수입니다.")
    private Long recordId;

    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String comment;
}
