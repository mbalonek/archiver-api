package de.kenolab.security.controller;

import de.kenolab.security.entity.ArchiveUser;
import de.kenolab.security.config.AuthenticationService;
import de.kenolab.security.config.UserAuthenticationProvider;
import de.kenolab.security.dto.AuthRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URI;

@RestController
@RequestMapping("/v1")
@Slf4j
public class AuthenticationController {

    private final UserAuthenticationProvider userAuthenticationProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    public AuthenticationController(UserAuthenticationProvider userAuthenticationProvider, BCryptPasswordEncoder passwordEncoder, AuthenticationService authenticationService) {
        this.userAuthenticationProvider = userAuthenticationProvider;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }

    /**
     * Authorize user, validate credentials, provides token
     * @param user (login=email, password)
     * @return HttpStatus 200 with token, 401 if the user credentials were false
     */
    @PostMapping(value = "/login")
    public ResponseEntity<String> loginUser(@AuthenticationPrincipal ArchiveUser user) {

        if (user != null) {
            user.setToken(userAuthenticationProvider.createToken(user.getEmail()));
            return ResponseEntity.ok(user.getToken());
        }
        return ResponseEntity.status(401).body(null);
    }

    /**
     * Register user in the application
     * @param authRequest composed of email and password
     * @return user -> HttpStatus 200 if registered successfully , 409 if user (based on email) already exists
     */
    @PostMapping(value = "/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid AuthRequest authRequest) throws MalformedURLException {

        ArchiveUser user = new ArchiveUser();

        ArchiveUser existingUser = authenticationService.findByLogin(authRequest.getEmail());

        if (existingUser == null) {

            String encodedPassword = passwordEncoder.encode(authRequest.getPassword());

            user.setEmail(authRequest.getEmail());
            user.setPassword(encodedPassword);
            user.setActive(true);
            user.setToken(userAuthenticationProvider.createToken(user.getEmail()));

            authenticationService.saveUser(user);
            URI uri= URI.create("/v1/login");
            return ResponseEntity.status(201).body("Log in " + uri);

        }
        user.setEmail(authRequest.getEmail());
        return ResponseEntity.status(409).body("User " + authRequest.getEmail() + " already exists. Log in your account");

    }

    /**
     *
     * @param archiveUser
     * @return
     */
    @PostMapping(value = "/logout")
    public ResponseEntity<String> logoutUser(@AuthenticationPrincipal ArchiveUser archiveUser){
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("You are logged out now");
    }
}
