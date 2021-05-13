package com.project.triport.service;

import com.project.triport.entity.Member;
import com.project.triport.jwt.CustomUserDetails;
import com.project.triport.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findByEmail(username);
        return member
                .map(CustomUserDetails::new) //this::createUserDetails
                .orElseThrow(() -> new UsernameNotFoundException(username + "-> 데이터베이스에서 찾을 수 없는 user 입니다."));
    }

    // DB에 Member 값이 존재한다면 UserDetails 객체로 만들어서 return
    private UserDetails createUserDetails(CustomUserDetails custom) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(custom.getAuthorities().toString());

        return new User(
                String.valueOf(custom.getUsername()),
                custom.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}
