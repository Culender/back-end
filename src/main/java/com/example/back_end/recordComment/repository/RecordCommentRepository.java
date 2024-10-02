package com.example.back_end.recordComment.repository;

import com.example.back_end.domain.RecordComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordCommentRepository extends JpaRepository<RecordComment, Long> {

    List<RecordComment> findByRecord_RecordId(Long recordId);
}
