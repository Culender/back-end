package com.example.back_end.record.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordListDto {
    private Long recordId;
    private String nickname;
    private String title;
    private String date;
    private String content;
    private String image;
    private Boolean isLiked;
    private Long likeCount;
    private Long commentCount;
}
