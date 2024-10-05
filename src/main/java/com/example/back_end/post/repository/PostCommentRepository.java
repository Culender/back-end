package com.example.back_end.post.repository;

import com.example.back_end.domain.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findByPostId(Long postId);
}
