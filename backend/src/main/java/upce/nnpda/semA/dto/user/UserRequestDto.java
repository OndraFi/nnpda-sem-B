package upce.nnpda.semA.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import upce.nnpda.semA.domain.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @NotNull(message = "Username cannot be null")
    private String username;

    @Email
    @NotNull(message = "Email cannot be null")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    @NotNull(message = "Password cannot be null")
    private String password;

    public User toUser(){
        return new User(username, email, password);
    }
}
