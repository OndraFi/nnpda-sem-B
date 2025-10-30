package upce.nnpda.semA.dto.ticketVersion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import upce.nnpda.semA.domain.TicketPriority;
import upce.nnpda.semA.domain.TicketState;
import upce.nnpda.semA.domain.TicketType;

import java.time.LocalDateTime;

@AllArgsConstructor
@Setter
@Getter
public class TicketVersionResponseDto {
    Long id;
    String title;
    TicketType type;
    TicketPriority priority;
    TicketState state;
    LocalDateTime createdAt;
}
