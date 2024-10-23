package com.hyunjin.kworld.ilchon.service;

import com.hyunjin.kworld.ilchon.dto.IlchonResponseDto;
import com.hyunjin.kworld.ilchon.entity.Ilchon;
import com.hyunjin.kworld.ilchon.repository.IlchonRepository;
import com.hyunjin.kworld.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IlchonService {
    private final IlchonRepository ilchonRepository;

    public boolean isIlchon(Member owner, Member currentMember) {
        return ilchonRepository.existsByMember1AndMember2(owner, currentMember) ||
                ilchonRepository.existsByMember1AndMember2(currentMember, owner);
    }

    @Transactional(readOnly = true)
    public List<IlchonResponseDto> getAllIlchon(Long currentMemberId){
        List<Ilchon> ilchonList = ilchonRepository.findByMember1IdOrMember2Id(currentMemberId, currentMemberId);

        return ilchonList.stream().map(ilchon -> {
            Member otherMember = ilchon.getMember1().getId().equals(currentMemberId) ? ilchon.getMember2() : ilchon.getMember1();
            return new IlchonResponseDto(
                    otherMember.getId(),
                    otherMember.getName(),
                    otherMember.getGender().toString(),
                    otherMember.getStudentNumber(),
                    otherMember.getMajor()
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public void deleteIlchon(Long currentMemberId, Long otherMemberId) {
        Ilchon ilchon = ilchonRepository.findByMemberIds(currentMemberId, otherMemberId)
                .orElseThrow(() -> new IllegalArgumentException("일촌이 아닙니다."));

        if (ilchon.getMember1().getId().equals(currentMemberId) || ilchon.getMember2().getId().equals(currentMemberId)) {
            ilchonRepository.delete(ilchon);
        } else {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }
}
