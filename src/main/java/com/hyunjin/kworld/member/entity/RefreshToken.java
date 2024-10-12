package com.hyunjin.kworld.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;
    private String token;
    private String email;

    public RefreshToken(String token, String email) {
        this.token = token;
        this.email = email;
    }

    public void updateToken(String token){
        this.token = token;
    }
}
