package de.kenolab.security.config;

import de.kenolab.entity.ArchiveUser;
import de.kenolab.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService{

    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public ArchiveUser findByLogin(String login){
        return userRepository.findByEmail(login);
    }

    public ArchiveUser authenticate(ArchiveUser request) {

        ArchiveUser user = userRepository.findByEmail(request.getEmail());
        if(user==null)
            return null;

        passwordEncoder().matches(request.getPassword(), user.getPassword());
        return user;
    }

    public ArchiveUser saveUser(ArchiveUser archiveUser){
        return userRepository.save(archiveUser);
    }

}


