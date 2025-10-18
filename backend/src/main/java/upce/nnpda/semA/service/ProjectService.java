package upce.nnpda.semA.service;

import org.springframework.stereotype.Service;
import upce.nnpda.semA.domain.Project;
import upce.nnpda.semA.domain.User;
import upce.nnpda.semA.dto.project.ProjectRequestDto;
import upce.nnpda.semA.dto.project.ProjectResponseDto;
import upce.nnpda.semA.dto.project.ProjectUpdateRequestDto;
import upce.nnpda.semA.exception.NotFoundException;
import upce.nnpda.semA.exception.OwnershipException;
import upce.nnpda.semA.repository.ProjectRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    public Project createProject(ProjectRequestDto projectRequest, User user) {
        Project project = projectRequest.toProject();
        project.setUser(user);
        return projectRepository.save(project);
    }

    public List<ProjectResponseDto> findAllProjectsByUser(User user) {
        List<Project> projects = this.projectRepository.findAllByUser(user);
        return projects.stream().map(Project::toResponseDto).collect(Collectors.toList());
    }

    public ProjectResponseDto findProjectById(Long projectId, User user) {
        Project project = this.projectRepository.findOneById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        if (!(project.getUser().getId() == user.getId())) {
            throw new OwnershipException("Project does not belong to the user");
        }
        return project.toResponseDto();
    }

    public void deleteProjectById(Long projectId, User user) {
        Project project = this.projectRepository.findOneById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        if (!(project.getUser().getId() == user.getId())) {
            throw new OwnershipException("Project does not belong to the user");
        }
        this.projectRepository.delete(project);
    }

    public ProjectResponseDto updateProjectById(Long projectId, ProjectUpdateRequestDto projectRequest, User user) {
        Project project = this.projectRepository.findOneById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));

        if (!(project.getUser().getId() == user.getId())) {
            throw new OwnershipException("Project does not belong to the user");
        }

        project.setName(projectRequest.getName());
        project.setDescription(projectRequest.getDescription());
        project.setState(projectRequest.getState());
        return this.projectRepository.save(project).toResponseDto();
    }
}
