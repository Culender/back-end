package com.example.back_end.record.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikedRecordDto {
    private Long recordId;
    private String title;
    private String content;
    private String place;
    private String date;
    private String image;
}
