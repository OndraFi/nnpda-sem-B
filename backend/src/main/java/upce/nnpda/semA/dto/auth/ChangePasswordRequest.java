package upce.nnpda.semA.dto.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ChangePasswordRequest {
    @Size(min = 6)
    @NotNull(message = "Old password cannot be null")
    private String oldPassword;

    @Size(min = 6)
    @NotNull(message = "New password cannot be null")
    private String newPassword;
}
