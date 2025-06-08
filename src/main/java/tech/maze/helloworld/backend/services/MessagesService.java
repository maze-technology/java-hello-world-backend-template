package tech.maze.helloworld.backend.services;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tech.maze.helloworld.backend.models.Message;
import tech.maze.helloworld.backend.repositories.MessagesRepository;

/**
 * Service class for managing Message entities.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MessagesService {
  MessagesRepository messagesRepository;

  /**
   * Retrieves all messages from the repository.
   *
   * @return a list of all messages stored in the repository
   */
  public List<Message> getMessages() {
    return messagesRepository.findAll();
  }

  /**
   * Saves the given message entity to the repository.
   *
   * @param message the message entity to be saved
   * @return the saved message entity
   */
  public Message saveMessage(Message message) {
    return messagesRepository.save(message);
  }
}
