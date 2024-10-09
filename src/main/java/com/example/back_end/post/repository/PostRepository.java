package com.example.back_end.post.repository;

import com.example.back_end.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategory(String category); // 카테고리별 게시글 조회 메서드
}
