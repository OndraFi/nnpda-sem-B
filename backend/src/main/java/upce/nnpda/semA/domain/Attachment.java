package upce.nnpda.semA.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = true)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = true)
    private Ticket ticket;

    @ManyToOne
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

}
