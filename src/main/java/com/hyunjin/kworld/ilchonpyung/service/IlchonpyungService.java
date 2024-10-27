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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IlchonpyungService {
    private final IlchonpyungRepository ilchonpyungRepository;
    private final MemberRepository memberRepository;
    private final IlchonRepository ilchonRepository;

    @Transactional
    public IlchonpyungResponseDto writeIlchonpyung(Long ownerId, Member writer, IlchonpyungRequestDto ilchonpyungRequestDto){
        Member owner = memberRepository.findById(ownerId)
                .orElseThrow(()-> new IllegalArgumentException("해당 사용자의 미니홈피가 존재하지 않습니다."));

        if(!ilchonRepository.existsByMember1AndMember2(writer, owner) && !ilchonRepository.existsByMember1AndMember2(owner, writer)){
            throw new IllegalArgumentException("일촌이 아닙니다");
        }

        boolean alreadyWritten = ilchonpyungRepository.existsByOwnerIdAndWriterId(ownerId, writer.getId());
        if (alreadyWritten) {
            throw new IllegalArgumentException("이미 작성한 일촌평이 있습니다.");
        }

        Ilchonpyung ilchonpyung = new Ilchonpyung(owner, writer, ilchonpyungRequestDto.getNickname(), ilchonpyungRequestDto.getIlchonpyung());
        ilchonpyungRepository.save(ilchonpyung);

        return new IlchonpyungResponseDto(ilchonpyung.getId(), ilchonpyungRequestDto.getNickname(), ilchonpyungRequestDto.getIlchonpyung());
    }

    @Transactional
    public List<IlchonpyungResponseDto> getAllIlchonpyung(Long ownerId){
        List<Ilchonpyung> ilchonpyungs = ilchonpyungRepository.findAllByOwnerId(ownerId);
        return ilchonpyungs.stream()
                .map(IlchonpyungResponseDto::new)
                .collect(Collectors.toList());
    }


    @Transactional
    public void deleteIlchonpyung(Long ownerId, Long ilchonpyungId, Member currentMember){
        Member owner = memberRepository.findById(ownerId)
                .orElseThrow(()-> new IllegalArgumentException("해당 사용자의 미니홈피가 존재하지 않습니다."));

        Ilchonpyung ilchonpyung = ilchonpyungRepository.findById(ilchonpyungId)
                .orElseThrow(() -> new IllegalArgumentException("일촌평이 존재하지 않습니다."));

        Member writer = ilchonpyung.getWriter();

        if (currentMember.getId().equals(writer.getId()) || currentMember.getId().equals(owner.getId())) {
            ilchonpyungRepository.delete(ilchonpyung);
        } else {
            throw new IllegalArgumentException("삭제할 권한이 없습니다.");
        }
    }
}
