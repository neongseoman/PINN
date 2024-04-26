package com.ssafy.be.auth.service;

import com.ssafy.be.auth.model.OAuth2UserInfo;
import com.ssafy.be.gamer.model.GamerDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Log4j2
public class OAuthServiceImpl extends DefaultOAuth2UserService implements OAuthService {
    private final com.ssafy.be.gamer.repository.GamerRepository GamerRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String,Object> oAuthUserAttribute =oAuth2User.getAttributes();
//        log.info(oAuthUserAttribute.);

        String registationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registationId,oAuthUserAttribute);

//        GamerDTO gamer = getOrSave()


        return null;
    }

//    private GamerDTO getOrSave() {}
}
