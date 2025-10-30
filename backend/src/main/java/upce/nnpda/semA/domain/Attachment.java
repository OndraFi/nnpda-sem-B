package upce.nnpda.semA.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import upce.nnpda.semA.dto.attachment.AttachmentResponseDto;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "app_attachments")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = true)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = true)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NonNull
    private String fileName;

    @NonNull
    private String fileType;

    @NonNull
    private Long fileSize;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] fileData;

    @NonNull
    private LocalDateTime createdAt;

    public AttachmentResponseDto toDto(){
        return new AttachmentResponseDto(fileData, fileName, fileType, fileSize, createdAt, user.getEmail());
    }

}
