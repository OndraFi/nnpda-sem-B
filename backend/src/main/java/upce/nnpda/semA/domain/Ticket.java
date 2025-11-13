package upce.nnpda.semA.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import upce.nnpda.semA.dto.ticket.TicketResponseDto;
import upce.nnpda.semA.dto.user.UserResponseDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "app_tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Size(min = 1, max = 160)
    private String title;

    @NonNull
    @Enumerated(EnumType.STRING)
    private TicketType type;

    @NonNull
    @Enumerated(EnumType.STRING)
    private TicketPriority priority;

    @NonNull
    @Enumerated(EnumType.STRING)
    private TicketState state = TicketState.OPEN;

    @NonNull
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "assignedUser_id", nullable = true)
    private User assignedUser = null;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Attachment> attachments;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<TicketVersion> versions = new ArrayList<>();

    public TicketResponseDto toDto() {
        UserResponseDto assignedUserDto = null;
        if(this.assignedUser != null) {
         assignedUserDto = this.assignedUser.toResponseDto();
        }
        return new TicketResponseDto(this.id, this.title,this.type, this.priority, this.state, assignedUserDto,this.project.getId(), this.versions.stream().map(TicketVersion::toDto).toList());
    }

}
