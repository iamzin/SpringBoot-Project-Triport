package com.project.triport.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member_promotion_history")
public class MemberPromotion extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false, name = "trils_promo")
    private boolean trilsFiveLikePromo;

    public MemberPromotion newMemberPromo(Member member) {
        return MemberPromotion.builder()
                .member(member)
                .trilsFiveLikePromo(false)
                .build();
    }

    public void updateTrilsPromo(Member member, boolean trilsFiveLikePromo) {
        this.member = member;
        this.trilsFiveLikePromo = trilsFiveLikePromo;
    }


}
