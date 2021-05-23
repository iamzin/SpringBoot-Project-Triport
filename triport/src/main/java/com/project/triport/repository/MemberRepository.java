package com.project.triport.repository;

import com.project.triport.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findMemberByEmail(String email);
    Optional<Member> findByEmail(String email);
    Optional<Member> findById(Long memberId);
    Optional<Member> findByKakaoId(Long kakaoId);
    boolean existsByEmail(String email); //중복 가입 방지
    boolean existsByNickname(String nickname); //닉네임 중복 방지
}
