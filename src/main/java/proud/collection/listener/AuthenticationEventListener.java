package proud.collection.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;
import proud.collection.service.SignupService;


@Component
public class AuthenticationEventListener {
    Logger logger = LoggerFactory.getLogger(AuthenticationEventListener.class);

    @Autowired
    private SignupService signupService;

    @EventListener
    public void authenticationFailed(AuthenticationFailureBadCredentialsEvent event){
        String username = (String) event.getAuthentication().getPrincipal();

        if(signupService.isUsernameAvailable(username)){
            logger.info("User with username: " + username + " does not exist");
        } else {
            logger.info("User with username: " + username + " has entered wrong password");
        }
    }
}
