package upce.nnpda.semA.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "app_ticket_versions")
public class TicketVersion {

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
    private TicketState state;

    @NonNull
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

}
