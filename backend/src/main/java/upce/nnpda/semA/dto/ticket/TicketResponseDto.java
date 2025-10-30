package upce.nnpda.semA.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import upce.nnpda.semA.domain.TicketPriority;
import upce.nnpda.semA.domain.TicketState;
import upce.nnpda.semA.domain.TicketType;
import upce.nnpda.semA.domain.TicketVersion;
import upce.nnpda.semA.dto.ticketVersion.TicketVersionResponseDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TicketResponseDto {
    private Long id;
    private String title;
    private TicketType type;
    private TicketPriority priority;
    private TicketState state;
    private List<TicketVersionResponseDto> versions;
}
