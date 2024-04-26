package com.ssafy.be.gamer.model;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor //기본 생성자 만들어줌
@Entity
@Getter
public class GamerDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    int gamerId;

    @Column
    String nickname;
    @Column
    String email;
    @Column(name="oauth_provider")
    String OAuthProvider;
    @Column(name="is_deleted")
    boolean isDeleted;
    @Column(name="image_url")
    String imageUrl;

}
