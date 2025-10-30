package upce.nnpda.semA.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import upce.nnpda.semA.dto.comment.CommentRequestDto;
import upce.nnpda.semA.dto.comment.CommentResponseDto;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "app_comments")
public class Comment {

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
    private String comment;

    @NonNull
    private LocalDateTime createdAt = LocalDateTime.now();

    public CommentResponseDto toDto(){
        return new CommentResponseDto(this.id, this.comment, this.createdAt, this.user.getEmail());
    }

}
