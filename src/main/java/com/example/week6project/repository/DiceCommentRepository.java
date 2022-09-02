package com.example.week6project.repository;

import com.example.week6project.domain.comments.DiceComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiceCommentRepository extends JpaRepository<DiceComment, Long> {
    List<DiceComment> findAllByOrderByModifiedAtDesc();
}
