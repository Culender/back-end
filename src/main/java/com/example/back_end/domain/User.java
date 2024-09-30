package com.example.back_end.domain;

import com.example.back_end.user.dto.SignUpDto;
import com.example.back_end.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Repository;

@Entity
@Table(name = "USER")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId; //유저 기본키

    @Column(name="loginId", nullable = false, unique = true)
    private String loginId; //로그인 아이디

    @Column(name="password", nullable = false)
    private String password; //비밀번호

    @Column(name="name",nullable = false)
    private String name; //이름

    @Column(name="nickname", nullable = false)
    private String nickname; //닉네임

    @Column(name="phone")
    private String phone; //연락처

    @Column(name="profile_img")
    private String profileImg;

    //유저 엔티티 생성 메소드
    public static User toEntity(SignUpDto dto, String encodePassword, String imgPath) {
        User user = User.builder()
                .loginId(dto.getLoginId())
                .password(encodePassword)
                .name(dto.getName())
                .nickname(dto.getNickname())
                .phone(dto.getPhone())
                .profileImg(imgPath)
                .build();
        return user;
    }
}
