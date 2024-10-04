package com.example.back_end.record.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class CreateRecordDto {
    @NotBlank(message = "제목은 필수 입력값입니다.")
    private String title;
    @NotBlank(message = "날짜는 필수 입력값입니다.")
    private String date;
    @NotBlank(message = "장소는 필수 입력값입니다.")
    private String place;
    @NotBlank(message = "내용은 필수 입력값입니다.")
    private String content;
    private MultipartFile image;
}
