package com.example.back_end.post.repository;

import com.example.back_end.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike,Long> {
    Long countByPost_PostId(Long postId);
    List<PostLike> findByUser_UserId(Long userId); // 특정 사용자가 좋아요한 Post 목록을 조회
    Optional<PostLike> findByPost_PostIdAndUser_UserId(Long postId, Long userId); // 게시글과 사용자ID를 사용해 좋아요 검색
}
