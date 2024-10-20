package com.hyunjin.kworld.ilchonRequest.service;

import com.hyunjin.kworld.ilchon.entity.Ilchon;
import com.hyunjin.kworld.ilchon.repository.IlchonRepository;
import com.hyunjin.kworld.ilchonRequest.dto.IlchonResponseDto;
import com.hyunjin.kworld.ilchonRequest.entity.IlchonRequest;
import com.hyunjin.kworld.ilchonRequest.entity.RequestStatus;
import com.hyunjin.kworld.ilchonRequest.repository.IlchonRequestRepository;
import com.hyunjin.kworld.member.entity.Member;
import com.hyunjin.kworld.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springdoc.webmvc.core.service.RequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IlchonRequestService {
    private final IlchonRequestRepository ilchonRequestRepository;
    private final MemberRepository memberRepository;
    private final RequestService requestBuilder;
    private final IlchonRepository ilchonRepository;

    @Transactional
    public IlchonRequest request(Long receiverId, Member requester) {
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(()->new IllegalArgumentException("가입하지 않은 회원입니다."));

        if(requester.getId().equals(receiver.getId())){
            throw new IllegalArgumentException("본인에겐 일촌 요청을 보낼 수 없습니다.");
        }

        boolean existingRequest = ilchonRequestRepository.existsByRequesterAndReceiver(requester, receiver) || ilchonRequestRepository.existsByRequesterAndReceiver(receiver, requester);
        if(existingRequest) {
            throw new IllegalArgumentException("일촌 요청 상태를 확인해보세요.");
        }

        boolean existingIlchon = ilchonRepository.existsByMember1AndMember2(requester, receiver) || ilchonRepository.existsByMember1AndMember2(receiver, requester);
        if(existingIlchon){
            throw new IllegalArgumentException("이미 일촌입니다.");
        }

        IlchonRequest ilchonRequest = new IlchonRequest(requester,receiver,RequestStatus.PENDING);
        return ilchonRequestRepository.save(ilchonRequest);
    }

    @Transactional
    public List<IlchonResponseDto> getRequests (Member receiver) {
        List<IlchonRequest> pendingRequests = ilchonRequestRepository.findByReceiverAndStatus(receiver, RequestStatus.PENDING);

        return pendingRequests.stream()
                .map(request -> new IlchonResponseDto(
                        request.getId(),
                        request.getRequester().getName(),
                        request.getRequester().getGender().toString(),
                        request.getRequester().getStudentNumber(),
                        request.getRequester().getMajor(),
                        request.getStatus()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void acceptRequest(Long requestId, Member receiver){
        IlchonRequest ilchonRequest = ilchonRequestRepository.findById(requestId)
                .orElseThrow(()->new IllegalArgumentException("요청을 찾을 수 없습니다."));

        if(!ilchonRequest.getReceiver().getId().equals(receiver.getId())){
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        Ilchon ilchon = new Ilchon(ilchonRequest.getRequester(), ilchonRequest.getReceiver());
        ilchonRepository.save(ilchon);

        ilchonRequestRepository.delete(ilchonRequest);
    }

    @Transactional
    public void rejectRequest(Long requestId, Member receiver){
        IlchonRequest ilchonRequest = ilchonRequestRepository.findById(requestId)
                .orElseThrow(()->new IllegalArgumentException("요청을 찾을 수 없습니다."));

        if(!ilchonRequest.getReceiver().getId().equals(receiver.getId())){
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        Ilchon ilchon = new Ilchon(ilchonRequest.getRequester(), ilchonRequest.getReceiver());
        ilchonRequestRepository.delete(ilchonRequest);
    }

}
