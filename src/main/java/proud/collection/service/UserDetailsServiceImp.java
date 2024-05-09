package proud.collection.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proud.collection.entity.Users;
import proud.collection.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserDetailsServiceImp implements UserDetailsService {


    Logger logger = LoggerFactory.getLogger(UserDetailsServiceImp.class);


    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {


        Users user = userRepository.findByUsername(username);


        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }


        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));

        logger.info(username + " has successfully logged in at "
                + Instant.now());


        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(),
                authorities);
    }
}

