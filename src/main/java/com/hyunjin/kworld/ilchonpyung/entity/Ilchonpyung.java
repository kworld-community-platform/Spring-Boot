package com.hyunjin.kworld.ilchonpyung.entity;

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
public class Ilchonpyung extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ilchonpyung_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Member owner;

    @ManyToOne
    @JoinColumn(name = "writer_id", nullable = false)
    private Member writer;

    @Column(nullable = false)
    private String ilchonpyung;

}
