package com.project.triport.repository;

import com.project.triport.entity.Member;
import com.project.triport.entity.MemberPromotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberPromotionRepository extends JpaRepository<MemberPromotion, Long> {
    MemberPromotion findByMember(Member member);
    void deleteByMember(Member member);
    void deleteByTrilsFiveLikePromo(boolean trilsPromo);
}
