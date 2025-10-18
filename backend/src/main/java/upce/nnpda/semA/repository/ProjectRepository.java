package upce.nnpda.semA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upce.nnpda.semA.domain.Project;
import upce.nnpda.semA.domain.User;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByUser(User user);

    Optional<Project> findOneById(Long projectId);
}
