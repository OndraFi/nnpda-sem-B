package upce.nnpda.semA.dto.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ResetPasswordRequest {
    @NotNull(message = "Code cannot be null")
    private String code;

    @Size(min = 6)
    @NotNull(message = "New password cannot be null")
    private String newPassword;
}
