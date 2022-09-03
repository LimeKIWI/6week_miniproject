package com.example.week6project.repository.comments;

import com.example.week6project.domain.comments.LottoComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LottoCommentRepository extends JpaRepository<LottoComment, Long> {
    List<LottoComment> findAllByOrderByModifiedAtDesc();
}
