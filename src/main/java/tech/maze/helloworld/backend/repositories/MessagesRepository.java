package tech.maze.helloworld.backend.repositories;

import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import tech.maze.helloworld.backend.models.Message;

/**
 * Repository interface for managing {@link Message} entities.
 */
@Repository
public interface MessagesRepository extends ListCrudRepository<Message, UUID> {

}
