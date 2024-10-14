package com.hyunjin.kworld.ilchonRequest.repository;

import com.hyunjin.kworld.ilchonRequest.entity.IlchonRequest;
import com.hyunjin.kworld.ilchonRequest.entity.RequestStatus;
import com.hyunjin.kworld.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IlchonRequestRepository extends JpaRepository<IlchonRequest,Long> {
       boolean existsByRequesterAndReceiver(Member requester, Member receiver);
       List<IlchonRequest> findByReceiverAndStatus(Member receiver, RequestStatus status);
}
