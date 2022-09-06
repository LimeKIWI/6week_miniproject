package com.example.week6project.repository.comments;

import com.example.week6project.domain.GameType;
import com.example.week6project.domain.Member;
import com.example.week6project.domain.comments.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByGameTypeEnumOrderByModifiedAtDesc(Enum<GameType> gameTypeEnum);
    List<Comment> findAllByMember(Member member);
}
