package upce.nnpda.semA.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import upce.nnpda.semA.domain.User;
import upce.nnpda.semA.dto.ticket.TicketRequestDto;
import upce.nnpda.semA.dto.ticket.TicketResponseDto;
import upce.nnpda.semA.dto.ticket.TicketUpdateRequestDto;
import upce.nnpda.semA.service.TicketService;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/v1/projects/{projectId}/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDto>> getAllTickets(@PathVariable Long projectId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<TicketResponseDto> tickets = this.ticketService.findAllTicketsByProject(projectId, user);
        return ResponseEntity.ok(tickets);
    }

    @PostMapping
    public ResponseEntity<TicketResponseDto> createTicket(
            @PathVariable Long projectId,
            @Valid @RequestBody TicketRequestDto ticketRequest) {
        log.info("Creating new ticket for projectId={}, data={}", projectId, ticketRequest);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TicketResponseDto createdTicket = this.ticketService.createTicket(projectId, ticketRequest, user);
        return ResponseEntity.ok(createdTicket);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketResponseDto> getTicketById(
            @PathVariable Long projectId,
            @PathVariable Long ticketId) {
        log.info("Fetching ticketId={} for projectId={}", ticketId, projectId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TicketResponseDto ticket = this.ticketService.findTicketById(projectId,ticketId,user);
        return ResponseEntity.ok(ticket);
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<TicketResponseDto> updateTicket(
            @PathVariable Long projectId,
            @PathVariable Long ticketId,
            @Valid @RequestBody TicketUpdateRequestDto ticketRequest) {
        log.info("Updating ticketId={} for projectId={}, data={}", ticketId, projectId, ticketRequest);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TicketResponseDto ticket = this.ticketService.updateTicketById(projectId, ticketId, ticketRequest, user);
        return ResponseEntity.ok(ticket);
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> deleteTicket(
            @PathVariable Long projectId,
            @PathVariable Long ticketId) {
        log.info("Deleting ticketId={} for projectId={}", ticketId, projectId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       this.ticketService.deleteTicketById(projectId, ticketId, user);
        return ResponseEntity.noContent().build();
    }
}
