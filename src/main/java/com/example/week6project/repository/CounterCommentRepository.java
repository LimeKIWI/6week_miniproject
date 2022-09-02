package com.example.week6project.repository;

import com.example.week6project.domain.comments.CounterComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterCommentRepository extends JpaRepository<CounterComment, Long> {
}
