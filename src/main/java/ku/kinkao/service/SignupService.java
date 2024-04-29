package ku.kinkao.service;


import ku.kinkao.dto.SignupRequest;
import ku.kinkao.entity.User;
import ku.kinkao.repository.MemberRepository;
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
    private MemberRepository repository;


    @Autowired
    private PasswordEncoder passwordEncoder;


    public boolean isUsernameAvailable(String username) {
        return repository.findByUsername(username) == null;
    }


    public void createMember(SignupRequest member) {
        User newMember = modelMapper.map(member, User.class);
        newMember.setCreatedAt(Instant.now());
        newMember.setRole("ROLE_USER");


        String hashedPassword = passwordEncoder.encode(member.getPassword());


        newMember.setPassword(hashedPassword);


        repository.save(newMember);
    }


    public User getMember(String username) {
        return repository.findByUsername(username);
    }
}

