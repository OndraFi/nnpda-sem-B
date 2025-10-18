package upce.nnpda.semA.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginRequest {
    @Email
    @NotNull(message = "Email cannot be null")
    private String email;

    @Size(min = 6)
    @NotNull(message = "Password cannot be null")
    private String password;

}