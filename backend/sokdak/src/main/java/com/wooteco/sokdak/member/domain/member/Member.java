package com.wooteco.sokdak.member.domain.member;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Username username;
    @Embedded
    private Password password;
    @Embedded
    private Nickname nickname;

    public Member() {
    }

    @Builder
    public Member(String username, String password, String nickname) {
        this.username = new Username(username);
        this.password = new Password(password);
        this.nickname = new Nickname(nickname);
    }
}
