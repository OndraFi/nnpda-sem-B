package upce.nnpda.semA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upce.nnpda.semA.domain.DeletedTicket;

public interface DeletedTicketRepository extends JpaRepository<DeletedTicket, Long> {

}
