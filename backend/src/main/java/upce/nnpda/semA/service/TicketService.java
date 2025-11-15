package upce.nnpda.semA.service;

import org.springframework.stereotype.Service;
import upce.nnpda.semA.domain.*;
import upce.nnpda.semA.dto.ticket.TicketRequestDto;
import upce.nnpda.semA.dto.ticket.TicketResponseDto;
import upce.nnpda.semA.dto.ticket.TicketUpdateRequestDto;
import upce.nnpda.semA.exception.NotFoundException;
import upce.nnpda.semA.exception.OwnershipException;
import upce.nnpda.semA.repository.DeletedTicketRepository;
import upce.nnpda.semA.repository.ProjectRepository;
import upce.nnpda.semA.repository.TicketRepository;
import upce.nnpda.semA.repository.TicketVersionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    TicketRepository ticketRepository;
    ProjectRepository projectRepository;
    TicketVersionRepository ticketVersionRepository;
    UserService userService;
    DeletedTicketRepository deletedTicketRepository;

    public TicketService(TicketRepository ticketRepository, ProjectRepository projectRepository, TicketVersionRepository ticketVersionRepository, UserService userService, DeletedTicketRepository deletedTicketRepository) {
        this.ticketRepository = ticketRepository;
        this.projectRepository = projectRepository;
        this.ticketVersionRepository = ticketVersionRepository;
        this.userService = userService;
        this.deletedTicketRepository = deletedTicketRepository;
    }

    public List<TicketResponseDto> findAllTicketsByProject(Long projectId, User user) {
        Project project = this.projectRepository.findOneById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        if (!(project.getUser().getId() == user.getId())) {
            throw new OwnershipException("Project does not belong to the user");
        }
        return project.getTickets().stream().map(Ticket::toDto).collect(Collectors.toList());

    }

    public TicketResponseDto createTicket(Long projectId, TicketRequestDto ticketRequest, User user) {
        Project project = this.projectRepository.findOneById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        if (!(project.getUser().getId() == user.getId())) {
            throw new OwnershipException("Project does not belong to the user");
        }
        Ticket ticket = ticketRequest.toTicket();
        ticket.setProject(project);
        this.ticketRepository.save(ticket);

        TicketVersion version = new TicketVersion();
        version.setTicket(ticket);
        version.setType(ticket.getType());
        version.setPriority(ticket.getPriority());
        version.setTitle(ticket.getTitle());
        version.setState(ticket.getState());
        this.ticketVersionRepository.save(version);

        // protože nestahuju nově ticket z db, musím přidat verzi ručně
        ticket.getVersions().add(version);
        return ticket.toDto();
    }

    public TicketResponseDto findTicketById(Long projectId, Long ticketId, User user) {
        Ticket ticket = this.ticketRepository.findOneById(ticketId).orElseThrow(
                () -> new NotFoundException("Ticket not found")
        );
        Project project = this.projectRepository.findOneById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        if(ticket.getAssignedUser() == null || ticket.getAssignedUser().getId() != user.getId()) {
            if (!(project.getUser().getId() == user.getId())) {
                throw new OwnershipException("Project does not belong to the user");
            }
        }

        if (ticket.getProject().getId() != project.getId()) {
            throw new OwnershipException("Ticket does not belong to this project");
        }
        return ticket.toDto();
    }

    public void deleteTicketById(Long projectId, Long ticketId, User user) {

        Ticket ticket = this.ticketRepository.findOneById(ticketId).orElseThrow(
                () -> new NotFoundException("Ticket not found")
        );

        Project project = this.projectRepository.findOneById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        if (!(project.getUser().getId() == user.getId())) {
            throw new OwnershipException("Project does not belong to the user");
        }

        if (!(ticket.getProject().getId() == project.getId())) {
            throw new OwnershipException("Ticket does not belong to this project");
        }

        DeletedTicket deletedTicket = new DeletedTicket();
        deletedTicket.setTicketId(ticket.getId());
        deletedTicket.setCreatedAt(LocalDateTime.now());
        this.deletedTicketRepository.save(deletedTicket);
        this.ticketRepository.delete(ticket);
    }

    public TicketResponseDto updateTicketById(Long projectId, Long ticketId, TicketUpdateRequestDto ticketRequest, User user) {
        Ticket ticket = this.ticketRepository.findOneById(ticketId).orElseThrow(
                () -> new NotFoundException("Ticket not found")
        );

        Project project = this.projectRepository.findOneById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        if(ticket.getAssignedUser() == null || ticket.getAssignedUser().getId() != user.getId()) {
            if (!(project.getUser().getId() == user.getId())) {
                throw new OwnershipException("Project does not belong to the user");
            }
        }

        if (!(ticket.getProject().getId() == project.getId())) {
            throw new OwnershipException("Ticket does not belong to this project");
        }

        TicketVersion version = new TicketVersion();
        version.setTicket(ticket);
        version.setTitle(ticketRequest.getTitle());
        version.setState(ticketRequest.getState());
        version.setType(ticketRequest.getType());
        version.setPriority(ticketRequest.getPriority());

        ticket.setTitle(ticketRequest.getTitle());
        ticket.setType(ticketRequest.getType());
        ticket.setState(ticketRequest.getState());
        ticket.setUpdatedAt(LocalDateTime.now());
        if(ticketRequest.getAssignedUserId() != null) {
            User assignedUser = this.userService.findById(ticketRequest.getAssignedUserId());
            assignedUser.setId(ticketRequest.getAssignedUserId());
            ticket.setAssignedUser(assignedUser);
        }
        this.ticketVersionRepository.save(version);
        ticket.getVersions().add(version);
        return this.ticketRepository.save(ticket).toDto();
    }
}