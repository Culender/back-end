package com.example.back_end.record.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyRecordListDto {
    private Long recordId;
    private Long year;
    private Long month;
    private Long day;
    //private Boolean isRecord;
}
