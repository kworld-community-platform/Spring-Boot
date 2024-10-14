package com.hyunjin.kworld.ilchonpyung.service;

import com.hyunjin.kworld.ilchon.repository.IlchonRepository;
import com.hyunjin.kworld.ilchonpyung.dto.IlchonpyungRequestDto;
import com.hyunjin.kworld.ilchonpyung.dto.IlchonpyungResponseDto;
import com.hyunjin.kworld.ilchonpyung.entity.Ilchonpyung;
import com.hyunjin.kworld.ilchonpyung.repository.IlchonpyungRepository;
import com.hyunjin.kworld.member.entity.Member;
import com.hyunjin.kworld.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IlchonpyungService {
    private final IlchonpyungRepository ilchonpyungRepository;
    private final MemberRepository memberRepository;
    private final IlchonRepository ilchonRepository;

    @Transactional
    public IlchonpyungResponseDto write(Long ownerId, Member writer, IlchonpyungRequestDto ilchonpyungRequestDto){
        Member owner = memberRepository.findById(ownerId)
                .orElseThrow(()-> new IllegalArgumentException("해당 사용자의 미니홈피가 존재하지 않습니다."));

        if(!ilchonRepository.existsByMember1AndMember2(writer, owner) && !ilchonRepository.existsByMember1AndMember2(owner, writer)){
            throw new IllegalArgumentException("일촌이 아닙니다");
        }

        Ilchonpyung ilchonpyung = new Ilchonpyung(owner, writer, ilchonpyungRequestDto.getNickname(), ilchonpyungRequestDto.getIlchonpyung());
        ilchonpyungRepository.save(ilchonpyung);

        return new IlchonpyungResponseDto(ilchonpyung.getId(), ilchonpyungRequestDto.getNickname(), ilchonpyungRequestDto.getIlchonpyung());
    }
}
