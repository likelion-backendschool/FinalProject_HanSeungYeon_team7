package com.example.mutbooks.app.member.repository;

import com.example.mutbooks.app.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByUsernameAndEmail(String username, String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
