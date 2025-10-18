package upce.nnpda.semA.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import upce.nnpda.semA.domain.ProjectState;

@Getter
@Setter
@AllArgsConstructor
public class ProjectResponseDto {
    private Long id;
    private String name;
    private String description;
    private ProjectState state;
}
