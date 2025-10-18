package upce.nnpda.semA.dto.project;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import upce.nnpda.semA.domain.ProjectState;

@Getter
public class ProjectUpdateRequestDto {
    @Size(min = 1, max = 120)
    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Description cannot be null")
    private String description;

    @NotNull(message = "State cannot be null")
    private ProjectState state;
}
