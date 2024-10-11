package com.example.back_end.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetLikedPostDto {
    private Long postId;
    private String title;
    private String content;
    private String nickname;
//    private String image;
}
