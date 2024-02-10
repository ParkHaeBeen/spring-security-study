package com.example.springsecuritystudy.auth.repository;

import com.example.springsecuritystudy.auth.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmail(String email);
}
