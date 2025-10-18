package upce.nnpda.semA.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import upce.nnpda.semA.domain.User;
import upce.nnpda.semA.dto.auth.*;
import upce.nnpda.semA.dto.user.UserRequestDto;
import upce.nnpda.semA.dto.user.UserResponseDto;
import upce.nnpda.semA.service.AuthenticationService;
import upce.nnpda.semA.service.JwtService;

@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @SecurityRequirements(value = {})
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto registerUserDto) {
        User registeredUser = authenticationService.register(registerUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser.toResponseDto());
    }

    @SecurityRequirements(value = {})
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginRequest loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        log.info("User authenticated: " + authenticatedUser.getEmail());

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @SecurityRequirements(value = {})
    @PostMapping("/request-password-reset")
    public ResponseEntity<String> requestPasswordReset(@Valid @RequestBody RequestPasswordResetRequest request) {
        String code = this.authenticationService.requestPasswordReset(request.getEmail());
        return ResponseEntity.ok(code);
    }

    @SecurityRequirements(value = {})
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        this.authenticationService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok("Password has been reset successfully.");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.authenticationService.changePassword(changePasswordRequest, user);
        return ResponseEntity.ok("Password has been changed successfully.");
    }
}