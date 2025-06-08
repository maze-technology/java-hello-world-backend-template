package tech.maze.helloworld.backend.controllers;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.maze.dtos.helloworld.payloads.AddRequest;
import tech.maze.dtos.helloworld.payloads.AddResponse;
import tech.maze.dtos.helloworld.payloads.GetAllRequest;
import tech.maze.dtos.helloworld.payloads.GetAllResponse;
import tech.maze.helloworld.backend.mappers.MessageMapper;
import tech.maze.helloworld.backend.models.Message;
import tech.maze.helloworld.backend.services.MessagesService;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HelloWorldGrpcControllerTest {
  @Mock
  private MessagesService messagesService;

  @Mock
  private MessageMapper messageMapper;

  @Mock
  private StreamObserver<AddResponse> addResponseObserver;

  @Mock
  private StreamObserver<GetAllResponse> getAllResponseObserver;

  private HelloWorldGrpcController controller;

  @BeforeEach
  void setUp() {
    controller = new HelloWorldGrpcController(messagesService, messageMapper);
  }

  @Test
  @DisplayName("Should save message and return response when adding a new message")
  void add_ShouldSaveMessageAndReturnResponse() {
    // Arrange
    final AddRequest request = AddRequest.newBuilder()
        .setContent("Test message")
        .build();
    final Message message = Message.builder()
        .id(UUID.randomUUID())
        .content("Test message")
        .build();
    final tech.maze.dtos.helloworld.models.Message messageDto = tech.maze.dtos.helloworld.models.Message.newBuilder()
        .setId(message.getId().toString())
        .setContent(message.getContent())
        .build();
    final AddResponse expectedResponse = AddResponse.newBuilder()
        .setMessage(messageDto)
        .build();

    when(messageMapper.toEntity(request)).thenReturn(message);
    when(messagesService.saveMessage(message)).thenReturn(message);
    when(messageMapper.toDto(message)).thenReturn(messageDto);

    // Act
    controller.add(request, addResponseObserver);

    // Assert
    verify(messageMapper).toEntity(request);
    verify(messagesService).saveMessage(message);
    verify(messageMapper).toDto(message);
    verify(addResponseObserver).onNext(expectedResponse);
    verify(addResponseObserver).onCompleted();
    verify(addResponseObserver, never()).onError(any());
  }

  @Test
  @DisplayName("Should return all messages when getting all messages")
  void getAll_ShouldReturnAllMessages() {
    // Arrange
    final GetAllRequest request = GetAllRequest.newBuilder().build();
    final Message message1 = Message.builder()
        .id(UUID.randomUUID())
        .content("Message 1")
        .build();
    final Message message2 = Message.builder()
        .id(UUID.randomUUID())
        .content("Message 2")
        .build();

    final tech.maze.dtos.helloworld.models.Message messageDto1 = tech.maze.dtos.helloworld.models.Message.newBuilder()
        .setId(message1.getId().toString())
        .setContent(message1.getContent())
        .build();
    final tech.maze.dtos.helloworld.models.Message messageDto2 = tech.maze.dtos.helloworld.models.Message.newBuilder()
        .setId(message2.getId().toString())
        .setContent(message2.getContent())
        .build();

    final GetAllResponse expectedResponse = GetAllResponse.newBuilder()
        .addAllMessages(List.of(messageDto1, messageDto2))
        .build();

    when(messagesService.getMessages()).thenReturn(List.of(message1, message2));
    when(messageMapper.toDto(message1)).thenReturn(messageDto1);
    when(messageMapper.toDto(message2)).thenReturn(messageDto2);

    // Act
    controller.getAll(request, getAllResponseObserver);

    // Assert
    verify(messagesService).getMessages();
    verify(messageMapper).toDto(message1);
    verify(messageMapper).toDto(message2);
    verify(getAllResponseObserver).onNext(expectedResponse);
    verify(getAllResponseObserver).onCompleted();
    verify(getAllResponseObserver, never()).onError(any());
  }
}
