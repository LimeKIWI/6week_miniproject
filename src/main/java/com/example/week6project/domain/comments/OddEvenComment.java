package com.example.week6project.domain.comments;

import com.example.week6project.controller.request.CommentRequestDto;
import com.example.week6project.domain.Member;
import com.example.week6project.domain.Timestamped;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OddEvenComment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String content;

    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }

    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }

}
