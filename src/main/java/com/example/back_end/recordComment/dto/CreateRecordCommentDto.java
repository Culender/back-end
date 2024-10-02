package com.example.back_end.recordComment.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "관람기록의 기본키는 필수입니다.")
    private Long recordId;

    @NotEmpty(message = "댓글 내용은 필수입니다.")
    private String comment;
}
