package proud.collection.service;


import proud.collection.dto.SignupRequest;
import proud.collection.entity.Users;
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
    private UserRepository repository;


    @Autowired
    private PasswordEncoder passwordEncoder;


    public boolean isUsernameAvailable(String username) {
        return repository.findByUsername(username) == null;
    }


    public void createMember(SignupRequest member) {
        Users newMember = modelMapper.map(member, Users.class);
        newMember.setCreatedAt(Instant.now());
        newMember.setRole("ROLE_USER");


        String hashedPassword = passwordEncoder.encode(member.getPassword());


        newMember.setPassword(hashedPassword);


        repository.save(newMember);
    }


    public Users getMember(String username) {
        return repository.findByUsername(username);
    }
}

