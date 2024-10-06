package com.example.back_end.recordComment.repository;

import com.example.back_end.domain.PostComment;
import com.example.back_end.domain.RecordComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecordCommentRepository extends JpaRepository<RecordComment, Long> {
    List<RecordComment> findByRecord_RecordId(Long recordId);
    Optional<RecordComment> findByRecord_RecordIdAndUser_UserId(Long recordId,Long userId);
    Long countByRecord_RecordId(Long recordId);
    List<RecordComment> findByUser_UserId(Long userId);
}
