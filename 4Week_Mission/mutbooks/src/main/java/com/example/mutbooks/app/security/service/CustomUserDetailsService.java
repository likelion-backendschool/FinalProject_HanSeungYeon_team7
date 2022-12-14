package com.example.mutbooks.app.security.service;

import com.example.mutbooks.app.security.dto.MemberContext;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username).get();

        List<GrantedAuthority> authorities = member.getAuthorities();

        return new MemberContext(member, authorities);
    }
}
