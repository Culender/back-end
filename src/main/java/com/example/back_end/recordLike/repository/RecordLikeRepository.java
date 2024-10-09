package com.example.back_end.recordLike.repository;

import com.example.back_end.domain.RecordLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecordLikeRepository extends JpaRepository<RecordLike, Long> {
    Optional<RecordLike> findByRecord_RecordIdAndUser_UserId(Long recordId, Long userId);
    List<RecordLike> findByUser_UserId(Long userId); // 사용자 ID로 좋아요한 기록 찾기
    Long countByRecord_RecordId(Long recordId);
}
