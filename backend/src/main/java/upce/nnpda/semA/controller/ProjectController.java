package upce.nnpda.semA.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import upce.nnpda.semA.domain.Project;
import upce.nnpda.semA.domain.User;
import upce.nnpda.semA.dto.project.ProjectRequestDto;
import upce.nnpda.semA.dto.project.ProjectResponseDto;
import upce.nnpda.semA.dto.project.ProjectUpdateRequestDto;
import upce.nnpda.semA.service.ProjectService;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getAllProjects() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Getting all projects");
        List<ProjectResponseDto> projects = this.projectService.findAllProjectsByUser(user);
        return ResponseEntity.ok(projects);
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(@Valid @RequestBody ProjectRequestDto projectRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Creating project with name={}", projectRequest.getName());
        Project createdProject = projectService.createProject(projectRequest, user);
        return ResponseEntity.ok(createdProject.toResponseDto());
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable Long projectId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Fetching project with id={}", projectId);
        ProjectResponseDto project = this.projectService.findProjectById(projectId, user);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDto> updateProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectUpdateRequestDto projectRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProjectResponseDto project = this.projectService.updateProjectById(projectId, projectRequest, user);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.projectService.deleteProjectById(projectId, user);
        return ResponseEntity.noContent().build();
    }
}
