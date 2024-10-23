package com.hyunjin.kworld.minihome.service;

import com.hyunjin.kworld.ilchon.service.IlchonService;
import com.hyunjin.kworld.ilchonpyung.dto.IlchonpyungResponseDto;
import com.hyunjin.kworld.member.entity.Member;
import com.hyunjin.kworld.member.repository.MemberRepository;
import com.hyunjin.kworld.minihome.dto.MinihomeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MinihomeService {
    private final MemberRepository memberRepository;
    private final IlchonService ilchonService;

    public MinihomeResponseDto getMinihome (Long ownerId, Member loginMember){
        Member owner = memberRepository.findById(ownerId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 미니홈피입니다."));

        boolean isIlchon = ilchonService.isIlchon(owner, loginMember);
        List<IlchonpyungResponseDto> ilchonpyungs = owner.getIlchonpyungs().stream()
                .map(IlchonpyungResponseDto::new)
                .toList();

        return new MinihomeResponseDto(owner, ilchonpyungs, isIlchon);
    }

}
