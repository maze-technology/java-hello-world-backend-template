package tech.maze.helloworld.backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.maze.helloworld.backend.models.Message;
import tech.maze.helloworld.backend.repositories.MessagesRepository;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessagesServiceTest {
  @Mock
  private MessagesRepository messagesRepository;

  private MessagesService messagesService;

  @BeforeEach
  void setUp() {
    messagesService = new MessagesService(messagesRepository);
  }

  @Test
  @DisplayName("Should return all messages from repository")
  void getMessages_ShouldReturnAllMessages() {
    // Arrange
    final Message message1 = new Message();
    message1.setId(UUID.randomUUID());
    message1.setContent("Message 1");
    final Message message2 = new Message();
    message2.setId(UUID.randomUUID());
    message2.setContent("Message 2");
    final List<Message> expectedMessages = List.of(message1, message2);

    when(messagesRepository.findAll()).thenReturn(expectedMessages);

    // Act
    final List<Message> actualMessages = messagesService.getMessages();

    // Assert
    assertThat(actualMessages).isEqualTo(expectedMessages);
    verify(messagesRepository).findAll();
  }

  @Test
  @DisplayName("Should save message and return saved message")
  void saveMessage_ShouldSaveAndReturnMessage() {
    // Arrange
    final Message message = new Message();
    message.setContent("Test message");
    final Message savedMessage = new Message();
    savedMessage.setId(UUID.randomUUID());
    savedMessage.setContent("Test message");

    when(messagesRepository.save(message)).thenReturn(savedMessage);

    // Act
    final Message result = messagesService.saveMessage(message);

    // Assert
    assertThat(result).isEqualTo(savedMessage);
    verify(messagesRepository).save(message);
  }
}
