package upce.nnpda.semA.dto.project;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import upce.nnpda.semA.domain.Project;

@Getter
public class ProjectRequestDto {
    @Size(min = 1, max = 120)
    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Description cannot be null")
    private String description;

    public Project toProject(){
        return new Project(name, description);
    }
}
