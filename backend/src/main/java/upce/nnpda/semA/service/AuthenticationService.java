package upce.nnpda.semA.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import upce.nnpda.semA.domain.User;
import upce.nnpda.semA.dto.auth.ChangePasswordRequest;
import upce.nnpda.semA.dto.auth.LoginRequest;
import upce.nnpda.semA.dto.auth.ResetPasswordRequest;
import upce.nnpda.semA.dto.user.UserRequestDto;
import upce.nnpda.semA.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String requestPasswordReset(@Email String email) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User with email " + email + " not found"));
        UUID resetCode = UUID.randomUUID();
        user.setResetCode(resetCode);
        user.setResetCodeExpiration(LocalDateTime.now().plusMinutes(10));
        this.userRepository.save(user);
        return resetCode.toString();
    }

    public User register(UserRequestDto userRequest) {
        if(userRepository.findByEmail(userRequest.getEmail()).isPresent()){
            throw new DuplicateKeyException("Email already in use");
        }
        if(userRepository.findByUsername(userRequest.getUsername()).isPresent()){
            throw new DuplicateKeyException("Username already in use");
        }
        User user = userRequest.toUser();
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        return userRepository.save(user);
    }

    public User authenticate(LoginRequest input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        User user = this.userRepository.findByResetCode(UUID.fromString(resetPasswordRequest.getCode()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid reset code"));
        if (user.getResetCodeExpiration().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reset code has expired");
        }
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        user.setResetCode(null);
        user.setResetCodeExpiration(null);
        this.userRepository.save(user);
    }

    public void changePassword(@Valid ChangePasswordRequest changePasswordRequest, User user) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        changePasswordRequest.getOldPassword()
                )
        );
        if(passwordEncoder.matches(changePasswordRequest.getNewPassword(), user.getPassword())){
            throw new IllegalArgumentException("New password must be different from the old password");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        this.userRepository.save(user);
    }
}
