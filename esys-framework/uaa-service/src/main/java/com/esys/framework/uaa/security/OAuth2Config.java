package com.esys.framework.uaa.security;

import com.esys.framework.core.configuration.EsysProperties;
import com.esys.framework.core.consts.ResultStatusCode;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.uaa.configuration.UaaProperties;
import com.esys.framework.uaa.web.error.CustomOauthException;
import com.esys.framework.uaa.web.error.MustChangePasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;


@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter{


    private static final int MIN_ACCESS_TOKEN_VALIDITY_SECS = 60;

    private BCryptPasswordEncoder passwordEncoder(){
        return  SecurityUtility.passwordEncoder();
    }

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private MessageSource messageSource;


    @Autowired
    private UaaProperties uaaProperties;

    @Autowired
    private EsysProperties esysProperties;


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {




/*
        int accessTokenValidity = uaaProperties.getWebClientConfiguration().getAccessTokenValidityInSeconds();
        accessTokenValidity = Math.max(accessTokenValidity, MIN_ACCESS_TOKEN_VALIDITY_SECS);
        int refreshTokenValidity = uaaProperties.getWebClientConfiguration().getRefreshTokenValidityInSecondsForRememberMe();
        refreshTokenValidity = Math.max(refreshTokenValidity, accessTokenValidity);

        clients.inMemory()
                .withClient(uaaProperties.getWebClientConfiguration().getClientId())
                .secret(passwordEncoder().encode(uaaProperties.getWebClientConfiguration().getSecret()))
                .scopes("openid")
                .autoApprove(true)
                .authorizedGrantTypes("implicit","refresh_token", "password", "authorization_code")
                .accessTokenValiditySeconds(accessTokenValidity)
                .refreshTokenValiditySeconds(refreshTokenValidity)
                .and()
                .withClient(esysProperties.getSecurity().getClientAuthorization().getClientId())
                .secret(passwordEncoder().encode(esysProperties.getSecurity().getClientAuthorization().getClientSecret()))
                .scopes("web-app")
                .autoApprove(true)
                .authorizedGrantTypes("client_credentials")
                .accessTokenValiditySeconds((int) esysProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds())
                .refreshTokenValiditySeconds((int) esysProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds());
*/



        clients.inMemory()
                .withClient("clientapp")
                .secret(new BCryptPasswordEncoder().encode("password"))
                .authorizedGrantTypes("refresh_token","password","client_credentials",
                        "authorization_code","implicit")
                .scopes("webclient","mobileclient");
    }



    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .exceptionTranslator(exception -> {
                    if (exception instanceof MustChangePasswordException) {
                        ModelResult result = new ModelResult.ModelResultBuilders(messageSource).
                                setStatus(ResultStatusCode.MUST_CHANGE_PASSWORD)
                                .build();
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(new CustomOauthException(exception.getMessage(), result));
                    } else if (exception instanceof OAuth2Exception) {
                        OAuth2Exception oAuth2Exception = (OAuth2Exception) exception;
                        int status = getStatusCode(oAuth2Exception.getOAuth2ErrorCode(), oAuth2Exception.getMessage());
                        ModelResult.ModelResultBuilders result = new ModelResult.ModelResultBuilders(messageSource).
                                setStatus(status);

                        if (status == ResultStatusCode.INVALID_GRANT) {
                            result.setMessageKey("uaa.wrongPassword");
                        }else  if (status == ResultStatusCode.USER_DISABLED) {
                            result.setMessageKey("uaa.userDisabled");
                        }else  if (status == ResultStatusCode.MUST_CHANGE_PASSWORD) {
                            result.setMessageKey("uaa.mustChangePassword");
                        }
                        return ResponseEntity
                                .status(oAuth2Exception.getHttpErrorCode())
                                .body(new CustomOauthException(oAuth2Exception.getMessage(), result.build()));
                    }
                    throw exception;
                });
        /*endpoints.authenticationManager(authenticationManager);*/

    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess(
                "isAuthenticated()");
    }


    private int getStatusCode(String code, String message) {
        if (code.equals(OAuth2Exception.INVALID_GRANT)) {
            if (message.equals("User is disabled")) {
                return ResultStatusCode.USER_DISABLED;
            }
            return ResultStatusCode.INVALID_GRANT;
        } else if (code.equals(OAuth2Exception.ACCESS_DENIED)) {
            return ResultStatusCode.ACCESS_DENIED;
        } else if (code.equals(OAuth2Exception.INVALID_REQUEST)) {
            if (message.equals("MustChangePasswordException")) {
                return ResultStatusCode.MUST_CHANGE_PASSWORD;
            }
            return ResultStatusCode.INVALID_REQUEST;
        } else if (code.equals(OAuth2Exception.INSUFFICIENT_SCOPE)) {
            return ResultStatusCode.INSUFFICIENT_SCOPE;
        } else if (code.equals(OAuth2Exception.INVALID_CLIENT)) {
            return ResultStatusCode.INVALID_CLIENT;
        } else if (code.equals(OAuth2Exception.REDIRECT_URI_MISMATCH)) {
            return ResultStatusCode.REDIRECT_URI_MISMATCH;
        } else if (code.equals(OAuth2Exception.UNSUPPORTED_RESPONSE_TYPE)) {
            return ResultStatusCode.UNSUPPORTED_RESPONSE_TYPE;
        } else if (code.equals(OAuth2Exception.UNSUPPORTED_GRANT_TYPE)) {
            return ResultStatusCode.UNSUPPORTED_GRANT_TYPE;
        } else if (code.equals(OAuth2Exception.UNAUTHORIZED_CLIENT)) {
            return ResultStatusCode.UNAUTHORIZED_CLIENT;
        } else if (code.equals(OAuth2Exception.INVALID_SCOPE)) {
            return ResultStatusCode.INVALID_SCOPE;
        } else if (code.equals(OAuth2Exception.INVALID_TOKEN)) {
            return ResultStatusCode.INVALID_TOKEN;
        }

        return ResultStatusCode.UNKNOWN_ERROR;
    }

}
