package com.example.back_end.record.repository;

import com.example.back_end.domain.Record;
import com.example.back_end.domain.RecordComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findAllByOrderByCreateAtDesc(); //최신순 정렬

    // 좋아요가 많은 순으로 조회
    @Query("SELECT r FROM Record r LEFT JOIN RecordLike rl ON r.recordId = rl.record.recordId GROUP BY r.recordId ORDER BY COUNT(rl) DESC, r.createAt DESC")
    List<Record> findAllByOrderByLikeCountDesc();

    List<Record> findAllByUser_UserId(Long userId);

    Optional<Record> findByRecordIdAndUser_UserId (Long recordId, Long userId);
}
