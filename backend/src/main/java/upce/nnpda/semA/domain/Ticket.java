package upce.nnpda.semA.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import upce.nnpda.semA.dto.ticket.TicketResponseDto;

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

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public TicketResponseDto toDto() {
        return new TicketResponseDto(this.id, this.title,this.type, this.priority, this.state);
    }

}
