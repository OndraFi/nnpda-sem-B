package upce.nnpda.semA.controller;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaTypeFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import upce.nnpda.semA.domain.Attachment;
import upce.nnpda.semA.domain.User;
import upce.nnpda.semA.dto.attachment.AttachmentResponseDto;
import upce.nnpda.semA.service.AttachmentService;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1")
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    // ===================== PROJECT ATTACHMENTS =====================

    @PostMapping(value = "/projects/{projectId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachmentResponseDto> uploadProjectAttachment(
            @PathVariable Long projectId,
            @RequestPart("file") @NotNull MultipartFile file
    ) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Attachment saved = attachmentService.saveForProject(projectId, user, file, LocalDateTime.now());
        URI location = URI.create("/api/v1/attachments/%d".formatted(saved.getId()));
        return ResponseEntity.created(location).body(saved.toDto());
    }

    @GetMapping("/projects/{projectId}/attachments")
    public ResponseEntity<List<AttachmentResponseDto>> listProjectAttachments(@PathVariable Long projectId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<AttachmentResponseDto> list = attachmentService.listForProject(projectId, user)
                .stream().map(Attachment::toDto).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/projects/{projectId}/attachments/{attachmentId}")
    public ResponseEntity<AttachmentResponseDto> getProjectAttachmentMeta(
            @PathVariable Long projectId,
            @PathVariable Long attachmentId
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Attachment attachment = attachmentService.findByIdForProject(projectId, attachmentId, user);
        return ResponseEntity.ok(attachment.toDto());
    }

    @DeleteMapping("/projects/{projectId}/attachments/{attachmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjectAttachment(
            @PathVariable Long projectId,
            @PathVariable Long attachmentId
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        attachmentService.deleteForProject(projectId, attachmentId, user);
    }

    // ===================== TICKET ATTACHMENTS =====================

    @PostMapping(value = "/projects/{projectId}/tickets/{ticketId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachmentResponseDto> uploadTicketAttachment(
            @PathVariable Long projectId,
            @PathVariable Long ticketId,
            @RequestPart("file") @NotNull MultipartFile file
    ) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Attachment saved = attachmentService.saveForTicket(projectId, ticketId, user, file, LocalDateTime.now());
        URI location = URI.create("/api/v1/attachments/%d".formatted(saved.getId()));
        return ResponseEntity.created(location).body(saved.toDto());
    }

    @GetMapping("/projects/{projectId}/tickets/{ticketId}/attachments")
    public ResponseEntity<List<AttachmentResponseDto>> listTicketAttachments(
            @PathVariable Long projectId,
            @PathVariable Long ticketId
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<AttachmentResponseDto> list = attachmentService.listForTicket(projectId, ticketId, user)
                .stream().map(Attachment::toDto).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/projects/{projectId}/tickets/{ticketId}/attachments/{attachmentId}")
    public ResponseEntity<AttachmentResponseDto> getTicketAttachmentMeta(
            @PathVariable Long projectId,
            @PathVariable Long ticketId,
            @PathVariable Long attachmentId
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Attachment attachment = attachmentService.findByIdForTicket(projectId, ticketId, attachmentId, user);
        return ResponseEntity.ok(attachment.toDto());
    }

    @DeleteMapping("/projects/{projectId}/tickets/{ticketId}/attachments/{attachmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTicketAttachment(
            @PathVariable Long projectId,
            @PathVariable Long ticketId,
            @PathVariable Long attachmentId
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        attachmentService.deleteForTicket(projectId, ticketId, attachmentId, user);
    }

}
