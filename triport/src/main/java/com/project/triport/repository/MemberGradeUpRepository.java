package com.project.triport.repository;

import com.project.triport.entity.Member;
import com.project.triport.entity.MemberGradeUp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberGradeUpRepository extends JpaRepository<MemberGradeUp, Long> {
    MemberGradeUp findByMember(Member member);
    void deleteByMember(Member member);
}
