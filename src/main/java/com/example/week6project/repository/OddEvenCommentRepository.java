package com.example.week6project.repository;

import com.example.week6project.domain.comments.OddEvenComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OddEvenCommentRepository extends JpaRepository<OddEvenComment, Long> {
}
