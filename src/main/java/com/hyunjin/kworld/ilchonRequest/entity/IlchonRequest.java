package com.hyunjin.kworld.ilchonRequest.entity;

import com.hyunjin.kworld.global.BaseEntity;
import com.hyunjin.kworld.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class IlchonRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ilchon_request_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private Member requester;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    public IlchonRequest(Member requester, Member receiver, RequestStatus status) {
        this.requester = requester;
        this.receiver = receiver;
        this.status = status;
    }
}
