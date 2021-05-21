package com.project.triport.repository;

import com.project.triport.entity.Member;
import com.project.triport.entity.MemberPromotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPromotionRepository extends JpaRepository<MemberPromotion, Long> {
    MemberPromotion findByMember(Member member);
}
