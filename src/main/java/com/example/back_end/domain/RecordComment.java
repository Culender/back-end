package com.example.back_end.domain;

import com.example.back_end.recordComment.dto.CreateRecordCommentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RECORD_COMMENT")
public class RecordComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="conmment_id")
    private Long commentId;

    @Column(name="content")
    private String content;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="record_id")
    private Record record;

    public static RecordComment toEntity(CreateRecordCommentDto recordCommentDto, User user, Record record) {
        return RecordComment.builder()
                .content(recordCommentDto.getComment())
                .user(user)
                .record(record)
                .build();
    }
}

