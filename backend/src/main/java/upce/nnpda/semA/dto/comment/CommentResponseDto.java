package upce.nnpda.semA.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String comment;
    private LocalDateTime createdAt;
    private String authorEmail;
}
