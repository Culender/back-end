package com.example.back_end.commentHistory.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentHistoryDto {
    private Long commentId;       // 댓글 ID
    private String content;       // 댓글 내용
    private String nickname;      // 작성자 닉네임
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private String type;          // 댓글 타입 (예: "POST", "RECORD")
}
