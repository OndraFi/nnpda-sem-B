package upce.nnpda.semA.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RequestPasswordResetRequest {
    @Email
    @NotNull(message = "Email cannot be null")
    private String email;
}
