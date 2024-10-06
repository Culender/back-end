package com.example.back_end.recordComment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordCommentListDto {
    private String nickname;
    private String comment;
    private String profileImg;
}
