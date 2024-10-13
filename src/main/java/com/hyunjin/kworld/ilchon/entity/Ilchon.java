package com.hyunjin.kworld.ilchon.entity;

import com.hyunjin.kworld.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Ilchon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ilchon_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member1_id")
    private Member member1;

    @ManyToOne
    @JoinColumn(name ="member2_id")
    private Member member2;
}
