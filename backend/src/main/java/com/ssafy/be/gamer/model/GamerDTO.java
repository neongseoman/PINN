package com.ssafy.be.gamer.model;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.be.oauth2.dto.OAuthType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@NoArgsConstructor //기본 생성자 만들어줌
@Entity
@Getter
@Table(name = "gamer")
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GamerDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gamer_id")
    int gamerId;
    @Column(name="oauth_provider")
    OAuthType OAuthProvider;
    @Column(name = "provider_id")
    Long providerId;
    @Column(name = "provider_nickname")
    String providerNickname;
    @Column
    String nickname;
    @Column(name="is_deleted")
    boolean isDeleted;
    @Column(name="image_url")
    String imageUrl;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public GamerDTO(OAuthType OAuthProvider, Long providerId, String providerNickname, String nickname, boolean isDeleted) {
        this.OAuthProvider = OAuthProvider;
        this.providerId = providerId;
        this.providerNickname = providerNickname;
        this.nickname = nickname;
        this.isDeleted = isDeleted;
        this.imageUrl = "hello/hello";
    }
}
