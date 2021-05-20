package com.project.triport.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.project.triport.entity.MemberGrade.TRAVELER;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member_grade")
public class MemberGradeUp extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberGrade memberGrade;

    public MemberGradeUp newMemberInfo(Member member) {
        return MemberGradeUp.builder()
                .member(member)
                .memberGrade(TRAVELER)
                .build();
    }

    public void gradeUpMember(Member member, MemberGrade memberGrade) {
        this.member = member;
        this.memberGrade = memberGrade;
    }
}
