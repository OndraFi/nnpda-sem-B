package upce.nnpda.semA.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import upce.nnpda.semA.domain.Comment;
import upce.nnpda.semA.domain.User;
import upce.nnpda.semA.dto.comment.CommentRequestDto;
import upce.nnpda.semA.dto.comment.CommentResponseDto;
import upce.nnpda.semA.service.CommentService;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1")
public class CommentController {

    CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @PostMapping("/projects/{projectId}/comments")
    public ResponseEntity<CommentResponseDto> addProjectComment(
            @PathVariable Long projectId,
            @Valid @RequestBody CommentRequestDto req
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment saved = commentService.addForProject(projectId, user, req);
        URI location = URI.create("/api/v1/comments/%d".formatted(saved.getId()));
        return ResponseEntity.created(location).body(saved.toDto());
    }

    @GetMapping("/projects/{projectId}/comments")
    public ResponseEntity<List<CommentResponseDto>> listProjectComments(
            @PathVariable Long projectId
    ) {
        List<CommentResponseDto> comments = commentService.listForProject(projectId).stream().map(Comment::toDto).toList();
        return ResponseEntity.ok(comments);
    }

    // === TICKET COMMENTS ===

    @PostMapping("/projects/{projectId}/tickets/{ticketId}/comments")
    public ResponseEntity<CommentResponseDto> addTicketComment(
            @PathVariable Long projectId,
            @PathVariable Long ticketId,
            @Valid @RequestBody CommentRequestDto req
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment saved = commentService.addForTicket(projectId, ticketId, user, req);
        URI location = URI.create("/api/v1/comments/%d".formatted( saved.getId()));
        return ResponseEntity.created(location).body(saved.toDto());
    }

    @GetMapping("/projects/{projectId}/tickets/{ticketId}/comments")
    public ResponseEntity<List<CommentResponseDto>> listTicketComments(
            @PathVariable Long projectId,
            @PathVariable Long ticketId
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<CommentResponseDto> comments = commentService.listForTicket(projectId, ticketId, user).stream().map(Comment::toDto).toList();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/projects/{projectId}/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> getCommentById(
            @PathVariable Long projectId,
            @PathVariable Long commentId
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = commentService.findById(commentId, projectId, user);
        return ResponseEntity.ok(comment.toDto());
    }

    @PutMapping("/projects/{projectId}/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long projectId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDto req
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment updated = commentService.updateComment(projectId, commentId, user, req);
        return ResponseEntity.ok(updated.toDto());
    }

}
