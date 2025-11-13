package upce.nnpda.semA.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import upce.nnpda.semA.domain.Ticket;
import upce.nnpda.semA.domain.User;
import upce.nnpda.semA.dto.ticket.TicketResponseDto;
import upce.nnpda.semA.dto.user.UserResponseDto;
import upce.nnpda.semA.service.UserService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/users")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> tickets = this.userService.findAllUsers();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/tickets/assigned")
    public ResponseEntity<List<TicketResponseDto>> getAssignedTickets() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<TicketResponseDto> tickets = this.userService.findAssignedTickets(user);
        return ResponseEntity.ok(tickets);
    }

}
