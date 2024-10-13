package com.hyunjin.kworld.ilchonRequest.service;

import com.hyunjin.kworld.ilchonRequest.entity.IlchonRequest;
import com.hyunjin.kworld.ilchonRequest.entity.RequestStatus;
import com.hyunjin.kworld.ilchonRequest.repository.IlchonRequestRepository;
import com.hyunjin.kworld.member.entity.Member;
import com.hyunjin.kworld.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IlchonRequestService {
    private final IlchonRequestRepository ilchonRequestRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public IlchonRequest request(Long receiverId, Member requester) {
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(()->new IllegalArgumentException("가입하지 않은 회원입니다."));

        if(requester.getId().equals(receiver.getId())){
            throw new IllegalArgumentException("본인에겐 일촌 요청을 보낼 수 없습니다.");
        }

        boolean existingRequest = ilchonRequestRepository.existsByRequesterAndReceiver(requester, receiver);
        if(existingRequest) {
            throw new IllegalArgumentException("이미 일촌 요청을 보냈습니다.");
        }

        IlchonRequest ilchonRequest = new IlchonRequest(requester,receiver,RequestStatus.PENDING);
        return ilchonRequestRepository.save(ilchonRequest);
    }
}
