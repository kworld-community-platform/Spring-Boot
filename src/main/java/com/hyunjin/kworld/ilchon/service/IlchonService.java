package com.hyunjin.kworld.ilchon.service;

import com.hyunjin.kworld.ilchon.entity.Ilchon;
import com.hyunjin.kworld.ilchon.repository.IlchonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IlchonService {
    private final IlchonRepository ilchonRepository;

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
