package com.example.back_end.post.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostDto {

    private Long postId;
    private String title;
    private String content;
    private MultipartFile image;
}
