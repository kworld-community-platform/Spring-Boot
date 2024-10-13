package com.hyunjin.kworld.member.repository;

import com.hyunjin.kworld.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("DB에 존재하는 계정")
    public void existEmail() {
        Member member = new Member("test@kw.ac.kr", "12kwukwu", "조광운");
        memberRepository.save(member);

        Optional<Member> findByEmail = memberRepository.findByEmail("test@kw.ac.kr");

        assertThat(findByEmail).isPresent();
        assertThat(findByEmail.get().getEmail()).isEqualTo("test@kw.ac.kr");
    }

    @Test
    @DisplayName("DB에 존재하지 않는 계정")
    public void nonExistEmail(){
        Optional<Member>findByEmail = memberRepository.findByEmail("kwu@kw.ac.kr");
        assertThat(findByEmail).isEmpty();
    }
}
