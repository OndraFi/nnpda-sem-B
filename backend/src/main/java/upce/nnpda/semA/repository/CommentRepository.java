package upce.nnpda.semA.repository;

import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import upce.nnpda.semA.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByProjectId(Long projectId);

    Optional<Comment> findOneById(long id);
}
