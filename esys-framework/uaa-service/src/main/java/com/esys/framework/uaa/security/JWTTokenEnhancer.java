package com.esys.framework.uaa.security;

import com.esys.framework.core.entity.uaa.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class JWTTokenEnhancer implements TokenEnhancer {


    public JWTTokenEnhancer(){
        int i = 1;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken token, OAuth2Authentication authentication) {
        Map<String,Object> info = new HashMap<>();
        User user = ((User)authentication.getPrincipal());
        info.put("id",user.getId());
        info.put("domain",user.getDomain());
        info.put("firstName",user.getFirstName());
        info.put("lastName",user.getLastName());

        ((DefaultOAuth2AccessToken) token).setAdditionalInformation(info);
        addClaims((DefaultOAuth2AccessToken) token);
        return token;
    }




    private void addClaims(DefaultOAuth2AccessToken accessToken) {
        DefaultOAuth2AccessToken token = accessToken;
        Map<String, Object> additionalInformation = token.getAdditionalInformation();
        if (additionalInformation.isEmpty()) {
            additionalInformation = new LinkedHashMap<String, Object>();
        }
        additionalInformation.put("iat", new Integer((int)(System.currentTimeMillis()/1000L)));
        token.setAdditionalInformation(additionalInformation);
    }
}
