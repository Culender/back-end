package com.example.back_end.recordComment.repository;

import com.example.back_end.domain.RecordComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordCommentRepository extends JpaRepository<RecordComment, Long> {
}
