package com.example.back_end.recordLike.repository;

import com.example.back_end.domain.RecordLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecordLikeRepository extends JpaRepository<RecordLike, Long> {
    Optional<RecordLike> findByRecord_RecordIdAndUser_UserId(Long recordId, Long userId);

}
