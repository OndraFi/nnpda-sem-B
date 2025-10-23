package upce.nnpda.semA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upce.nnpda.semA.domain.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
