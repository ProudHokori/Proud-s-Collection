package proud.collection.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import proud.collection.entity.ConfirmationToken;
import proud.collection.entity.Users;
import proud.collection.repository.ConfirmationTokenRepository;
import proud.collection.repository.UserRepository;

@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            Users user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            user.setEnabled(true);
            userRepository.save(user);
            logger.info("User id: " + user.getId() + " has been verified with email: " + user.getEmail());
            return ResponseEntity.ok("Email verified successfully!");
        }
        return ResponseEntity.badRequest().body("Error: Couldn't verify email");
    }

    public void updateResetPasswordToken(String token, String email) {
        Users user = userRepository.findByEmailIgnoreCase(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
            logger.info("User id: " + user.getId() + " has been updated with reset password token: " + token);
        }else{
            throw new RuntimeException("User not found");
        }
    }

    public Users getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(Users user, String newPassword) {

        String hashedPassword = passwordEncoder.encode(newPassword);

        user.setPassword(hashedPassword);
        user.setResetPasswordToken(null);
        userRepository.save(user);

        logger.info("User id: " + user.getId() + " has updated their password");
    }
}
