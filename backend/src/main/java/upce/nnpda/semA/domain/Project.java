package upce.nnpda.semA.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import upce.nnpda.semA.dto.project.ProjectResponseDto;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "app_projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Size(min = 1, max = 120)
    private String name;

    @NonNull
    private String description;

    @NonNull
    @Enumerated(EnumType.STRING)
    private ProjectState state = ProjectState.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Attachment> attachments;

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", state=" + state +
                ", user=" + user +
                '}';
    }

    public ProjectResponseDto toResponseDto(){
        return new ProjectResponseDto(this.id,this.name,this.description,this.state);
    }
}
