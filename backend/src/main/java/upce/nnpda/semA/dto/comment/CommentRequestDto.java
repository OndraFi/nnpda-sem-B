package upce.nnpda.semA.dto.comment;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import upce.nnpda.semA.domain.Comment;

@Getter
public class CommentRequestDto {
    @Min(1)
    String comment;
}
