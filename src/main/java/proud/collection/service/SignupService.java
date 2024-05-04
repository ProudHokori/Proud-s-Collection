package proud.collection.service;


import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import proud.collection.dto.SignupRequest;
import proud.collection.entity.ConfirmationToken;
import proud.collection.entity.Users;
import proud.collection.repository.ConfirmationTokenRepository;
import proud.collection.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

@Service
public class SignupService {


    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;


    public boolean isUsernameAvailable(String username) {
        return userRepository.findByUsername(username) == null;
    }

    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }


    public void createMember(SignupRequest member) {
        Users newMember = modelMapper.map(member, Users.class);
        newMember.setCreatedAt(Instant.now());
        newMember.setRole("ROLE_USER");


        String hashedPassword = passwordEncoder.encode(member.getPassword());

        newMember.setPassword(hashedPassword);
        newMember.setEnabled(false);
        userRepository.save(newMember);

        ConfirmationToken confirmationToken = new ConfirmationToken();

        confirmationToken.setUser(newMember);
        confirmationToken.setCreatedDate(Instant.now());
        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newMember.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:8090/user/confirm-account?token="+confirmationToken.getConfirmationToken());
        emailService.sendEmail(mailMessage);

        System.out.println("Confirmation Token: " + confirmationToken.getConfirmationToken());
    }


    public Users getMember(String username) {
        return userRepository.findByUsername(username);
    }
}

