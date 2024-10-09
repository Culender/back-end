package com.example.back_end.record.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyRecordListDto {
    private Long recordId;
    private String year;
    private String month;
    private String day;
    //private Boolean isRecord;
}
