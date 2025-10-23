package upce.nnpda.semA.service;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Optionals;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upce.nnpda.semA.domain.Comment;
import upce.nnpda.semA.domain.Project;
import upce.nnpda.semA.domain.Ticket;
import upce.nnpda.semA.domain.User;
import upce.nnpda.semA.dto.comment.CommentRequestDto;
import upce.nnpda.semA.exception.NotFoundException;
import upce.nnpda.semA.exception.OwnershipException;
import upce.nnpda.semA.repository.CommentRepository;
import upce.nnpda.semA.repository.ProjectRepository;
import upce.nnpda.semA.repository.TicketRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentService {

    CommentRepository commentRepository;
    ProjectRepository projectRepository;
    TicketRepository ticketRepository;

    public Comment addForProject(Long projectId, User user, @Valid CommentRequestDto req) {
        Project project = this.projectRepository.findOneById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        if (!(project.getUser().getId() == user.getId())) {
            throw new OwnershipException("Project does not belong to the user");
        }

        Comment comment = new Comment();
        comment.setComment(req.getComment());
        comment.setProject(project);
        comment.setUser(user);
        this.commentRepository.save(comment);
        return comment;
    }

    @Transactional(readOnly = true)
    public List<Comment> listForProject(Long projectId) {
        return commentRepository.findByProjectId(projectId);
    }

    public Comment addForTicket(Long projectId, Long ticketId, User user, @Valid CommentRequestDto req) {
        Project project = this.projectRepository.findOneById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        if (!(project.getUser().getId() == user.getId())) {
            throw new OwnershipException("Project does not belong to the user");
        }

        Ticket ticket = this.ticketRepository.findOneById(ticketId).orElseThrow(() -> new NotFoundException("Ticket not found"));

        if (!(ticket.getProject().getId() == project.getId())) {
            throw new OwnershipException("Ticket does not belong to the project");
        }

        Comment comment = new Comment();
        comment.setComment(req.getComment());
        comment.setProject(project);
        comment.setUser(user);
        this.commentRepository.save(comment);
        return comment;
    }

    public List<Comment> listForTicket(Long projectId, Long ticketId, User user) {
        Project project = this.projectRepository.findOneById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        if (!(project.getUser().getId() == user.getId())) {
            throw new OwnershipException("Project does not belong to the user");
        }

        Ticket ticket = this.ticketRepository.findOneById(ticketId).orElseThrow(() -> new NotFoundException("Ticket not found"));

        if (!(ticket.getProject().getId() == project.getId())) {
            throw new OwnershipException("Ticket does not belong to the project");
        }

        return ticket.getComments();
    }

    public Comment findById(Long commentId, Long projectId, User user) {
        Project project = this.projectRepository.findOneById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        if (!(project.getUser().getId() == user.getId())) {
            throw new OwnershipException("Project does not belong to the user");
        }

        return commentRepository.findOneById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));

    }

    public Comment updateComment(Long projectId, Long commentId, User user, @Valid CommentRequestDto req) {
        Project project = this.projectRepository.findOneById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        if (!(project.getUser().getId() == user.getId())) {
            throw new OwnershipException("Project does not belong to the user");
        }

        Comment comment = commentRepository.findOneById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        if(!(comment.getUser().getId() == user.getId())){
            throw new OwnershipException("Comment does not belong to the user");
        }

        comment.setComment(req.getComment());
        commentRepository.save(comment);
        return comment;
    }
}
