package com.example.back_end.domain;

import com.example.back_end.record.dto.CreateRecordDto;
import com.example.back_end.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "RECORD")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Record extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="record_id")
    private Long recordId;

    @Column(name="date")
    private String date;

    @Column(name="place")
    private String place;

    @Column(name="content")
    private String content;

    @Column(name="image")
    private String image;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    private User user;

    public static Record toEntity(CreateRecordDto createPostDto, User user, String imgPath) {
        return Record.builder()
                .user(user)
                .date(createPostDto.getDate())
                .place(createPostDto.getPlace())
                .content(createPostDto.getContent())
                .image(imgPath)
                .build();
    }
}
