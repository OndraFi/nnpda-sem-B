package upce.nnpda.semA.dto.attachment;

import lombok.AllArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@AllArgsConstructor
public class AttachmentResponseDto {
    byte[] data;
    String name;
    String type;
    Long size;
    LocalDateTime createdAt;
    String authorEmail;
}
