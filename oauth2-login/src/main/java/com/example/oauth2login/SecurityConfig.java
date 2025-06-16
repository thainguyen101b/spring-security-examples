package com.example.oauth2login;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // auto-config
//         http.oauth2Login(Customizer.withDefaults());

        http.oauth2Login(oauth2 -> oauth2
                .loginPage("/login/oauth2")
//                 .redirectionEndpoint(redirection -> redirection
//                        .baseUri("/login/oauth2/code/*"))
                .userInfoEndpoint(userinfo -> userinfo
                        .userAuthoritiesMapper(this.userAuthoritiesMapper())));
        return http.build();
    }

    private GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return authorities -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            for (GrantedAuthority authority : authorities) {
                if (authority instanceof OAuth2UserAuthority oauth2Authority) {
                    Map<String, Object> attributes = oauth2Authority.getAttributes();

                    // Map the attributes found in userAttributes
                    // to one or more GrantedAuthority's and add it to mappedAuthorities

                    // For example
                    String email = (String) attributes.get("email");

                    if (email != null && email.endsWith("@admin.com")) {
                        mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                    } else {
                        mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                    }
                } else if (authority instanceof OidcUserAuthority oidcAuthority) {
                    OidcIdToken idToken = oidcAuthority.getIdToken();
                    OidcUserInfo userInfo = oidcAuthority.getUserInfo();

                    // Map the claims found in idToken and/or userInfo
                    // to one or more GrantedAuthority's and add it to mappedAuthorities
                }
            }

            return mappedAuthorities;
        };
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
    }

    @Bean
    public ClientRegistration googleClientRegistration() {
//        return ClientRegistration.withRegistrationId("google")
//                .clientId("${GOOGLE_CLIENT_ID}")
//                .clientSecret("${GOOGLE_CLIENT_SECRET}")
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .redirectUri("{baseUrl}/login/oauth2/code2/{registrationId}")
//                .scope("openid", "profile", "email", "address", "phone")
//                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
//                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
//                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
//                .userNameAttributeName(IdTokenClaimNames.SUB)
//                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
//                .clientName("Google")
//                .build();

        return CommonOAuth2Provider.GOOGLE.getBuilder("google")
                .clientId("${GOOGLE_CLIENT_ID}")
                .clientSecret("${GOOGLE_CLIENT_SECRET}")
                .redirectUri("http://localhost:8080/login/oauth2/code/google")
                .build();
    }

}
