package upce.nnpda.semA.service;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import upce.nnpda.semA.domain.Attachment;
import upce.nnpda.semA.domain.Project;
import upce.nnpda.semA.domain.Ticket;
import upce.nnpda.semA.domain.User;
import upce.nnpda.semA.exception.NotFoundException;
import upce.nnpda.semA.exception.OwnershipException;
import upce.nnpda.semA.repository.AttachmentRepository;
import upce.nnpda.semA.repository.ProjectRepository;
import upce.nnpda.semA.repository.TicketRepository;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@AllArgsConstructor
public class AttachmentService {


    private final AttachmentRepository attachmentRepository;
    private final ProjectRepository projectRepository;
    private final TicketRepository ticketRepository;


// ===================== PROJECT =====================


    @Transactional
    public Attachment saveForProject(Long projectId, User user, MultipartFile file, LocalDateTime createdAt) throws IOException {
        Project project = projectRepository.findOneById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));
        if (project.getUser().getId() != user.getId()) {
            throw new OwnershipException("Project does not belong to the user");
        }
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        Attachment a = new Attachment();
        a.setProject(project);
        a.setUser(user);
        a.setFileName(Objects.requireNonNull(file.getOriginalFilename()));
        a.setFileType(Objects.requireNonNull(file.getContentType()));
        a.setFileData(file.getBytes());
        a.setCreatedAt(LocalDateTime.now());
        return attachmentRepository.save(a);
    }


    @Transactional(readOnly = true)
    public List<Attachment> listForProject(Long projectId, User user) {
        Project project = projectRepository.findOneById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));
        if (project.getUser().getId() != user.getId()) {
            throw new OwnershipException("Project does not belong to the user");
        }
        return attachmentRepository.findByProjectId(projectId);
    }


    @Transactional(readOnly = true)
    public Attachment findByIdForProject(Long projectId, Long attachmentId, User user) {
        Project project = projectRepository.findOneById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));
        if (project.getUser().getId() != user.getId()) {
            throw new OwnershipException("Project does not belong to the user");
        }
        Attachment a = attachmentRepository.findOneById(attachmentId)
                .orElseThrow(() -> new NotFoundException("Attachment not found"));

        if (a.getProject() == null || a.getProject().getId() != projectId) {
            throw new OwnershipException("Attachment does not belong to the project");
        }
        return a;
    }

    public void deleteForProject(Long projectId, Long attachmentId, User user) {
        Project project = projectRepository.findOneById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));
        if (project.getUser().getId() != user.getId()) {
            throw new OwnershipException("Project does not belong to the user");
        }

        Attachment a = attachmentRepository.findOneById(attachmentId)
                .orElseThrow(() -> new NotFoundException("Attachment not found"));

        if (a.getProject() == null || a.getProject().getId() != projectId) {
            throw new OwnershipException("Attachment does not belong to the project");
        }
        attachmentRepository.delete(a);
    }

    public Attachment saveForTicket(Long projectId, Long ticketId, User user, @NotNull MultipartFile file, LocalDateTime now) throws IOException {
        Project project = projectRepository.findOneById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));
        if (project.getUser().getId() != user.getId()) {
            throw new OwnershipException("Project does not belong to the user");
        }
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        Ticket ticket = ticketRepository.findOneById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        if (ticket.getProject() == null || ticket.getProject().getId() != projectId) {
            throw new OwnershipException("Ticket does not belong to the project");
        }

        Attachment a = new Attachment();
        a.setProject(project);
        a.setUser(user);
        a.setFileName(Objects.requireNonNull(file.getOriginalFilename()));
        a.setFileType(Objects.requireNonNull(file.getContentType()));
        a.setFileData(file.getBytes());
        a.setCreatedAt(LocalDateTime.now());
        return attachmentRepository.save(a);

    }

    public List<Attachment> listForTicket(Long projectId, Long ticketId, User user) {
        Ticket ticket = ticketRepository.findOneById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        Project project =
                this.projectRepository.findOneById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        if (ticket.getAssignedUser() == null || ticket.getAssignedUser().getId() != user.getId()) {
            if (!(project.getUser().getId() == user.getId())) {
                throw new OwnershipException("Project does not belong to the user");
            }
        }


        if (ticket.getProject() == null || ticket.getProject().getId() != projectId) {
            throw new OwnershipException("Ticket does not belong to the project");
        }

        return attachmentRepository.findByTicket_Id(ticketId);

    }


    public Attachment findByIdForTicket(Long projectId, Long ticketId, Long attachmentId, User user) {
        Project project = projectRepository.findOneById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));
        if (project.getUser().getId() != user.getId()) {
            throw new OwnershipException("Project does not belong to the user");
        }
        Ticket ticket = ticketRepository.findOneById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        if (ticket.getProject() == null || ticket.getProject().getId() != projectId) {
            throw new OwnershipException("Ticket does not belong to the project");
        }

        return attachmentRepository.findById(attachmentId).orElseThrow(() -> new NotFoundException("Attachment not found"));
    }

    public void deleteForTicket(Long projectId, Long ticketId, Long attachmentId, User user) {

        Project project = projectRepository.findOneById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));
        if (project.getUser().getId() != user.getId()) {
            throw new OwnershipException("Project does not belong to the user");
        }
        Ticket ticket = ticketRepository.findOneById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        if (ticket.getProject() == null || ticket.getProject().getId() != projectId) {
            throw new OwnershipException("Ticket does not belong to the project");
        }

        Attachment a = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new NotFoundException("Attachment not found"));

        attachmentRepository.delete(a);
    }
}