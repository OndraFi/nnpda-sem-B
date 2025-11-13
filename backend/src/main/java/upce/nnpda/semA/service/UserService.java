package upce.nnpda.semA.service;

import org.springframework.stereotype.Service;
import upce.nnpda.semA.domain.Ticket;
import upce.nnpda.semA.domain.User;
import upce.nnpda.semA.dto.ticket.TicketResponseDto;
import upce.nnpda.semA.dto.user.UserResponseDto;
import upce.nnpda.semA.exception.NotFoundException;
import upce.nnpda.semA.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDto> findAllUsers() {
        List<User> users = this.userRepository.findAll();
        return users.stream().map(User::toResponseDto).toList();
    }

    public User findById(Long assignedUserId) {
        return this.userRepository.findById(assignedUserId).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public List<TicketResponseDto> findAssignedTickets(User user) {
        User u = this.userRepository.findById(user.getId()).orElseThrow(() -> new NotFoundException("User not found"));
        return u.getAssignedTickets().stream().map(Ticket::toDto).toList();
    }
}