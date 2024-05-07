package proud.collection.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import proud.collection.entity.Users;
import proud.collection.repository.UserRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Configuration
public class UserServicesConfiguration {

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return new OAuth2UserServiceImpl();
    }

    @Bean
    public OidcUserService oidcUserService() {
        return new OidcUserServiceImpl();
    }

    @Autowired
    private UserRepository userRepository;

    private class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
        private final DefaultOAuth2UserService delegate =
                new DefaultOAuth2UserService();

        @Override
        public OAuth2User loadUser(OAuth2UserRequest request)
                throws OAuth2AuthenticationException {

            String attribute = request.getClientRegistration()
                    .getProviderDetails()
                    .getUserInfoEndpoint()
                    .getUserNameAttributeName();
            OAuth2User user = delegate.loadUser(request);

            try {
                user = new DefaultOAuth2User(new ArrayList<>(),
                        user.getAttributes(), attribute);

            } catch (OAuth2AuthenticationException exception) {
                throw exception;
            }

            return user;
        }
    }

    private class OidcUserServiceImpl extends OidcUserService {

        {
            setOauth2UserService(oAuth2UserService());
        }

        @Override
        public OidcUser loadUser(OidcUserRequest request)
                throws OAuth2AuthenticationException {
            String attribute = request.getClientRegistration()
                    .getProviderDetails()
                    .getUserInfoEndpoint()
                    .getUserNameAttributeName();
            OidcUser user = super.loadUser(request);

            try {
                user = new DefaultOidcUser(new ArrayList<>(),
                        user.getIdToken(), user.getUserInfo(), attribute);
                Users userEmail = userRepository.findByEmailIgnoreCase(user.getAttributes().get("email").toString());
                Users userName = userRepository.findByUsername(user.getAttributes().get("name").toString());
                if (userEmail == null && userName == null) {
                    Users repoUser = new Users();
                    repoUser.setEmail(user.getAttributes().get("email").toString());
                    repoUser.setUsername(user.getAttributes().get("name").toString());
                    repoUser.setCreatedAt(Instant.now());
                    repoUser.setRole("ROLE_USER");
                    repoUser.setEnabled(true);
                    userRepository.save(repoUser);
                }
                Collection<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                return new DefaultOidcUser(authorities, user.getIdToken(), user.getUserInfo(), attribute);


            } catch (OAuth2AuthenticationException exception) {
                throw exception;
            }
        }
    }
}
