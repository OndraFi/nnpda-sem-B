package upce.nnpda.semA.dto.ticket;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import upce.nnpda.semA.domain.Ticket;
import upce.nnpda.semA.domain.TicketPriority;
import upce.nnpda.semA.domain.TicketType;

@Getter
public class TicketRequestDto {
    @Size(min = 1, max = 160)
    @NotNull(message = "Title cannot be null")
    private String title;

    @NotNull(message = "Priority cannot be null")
    private TicketPriority priority;

    @NotNull(message = "Type cannot be null")
    private TicketType type;

    public Ticket toTicket(){
        return new Ticket(title,type,priority);
    }
}
