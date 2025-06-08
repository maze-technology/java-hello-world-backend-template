package tech.maze.helloworld.backend.controllers;

import io.grpc.stub.StreamObserver;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.devh.boot.grpc.server.service.GrpcService;
import tech.maze.dtos.helloworld.controllers.HelloWorldGrpc;
import tech.maze.dtos.helloworld.payloads.AddRequest;
import tech.maze.dtos.helloworld.payloads.AddResponse;
import tech.maze.dtos.helloworld.payloads.GetAllRequest;
import tech.maze.dtos.helloworld.payloads.GetAllResponse;
import tech.maze.helloworld.backend.mappers.MessageMapper;
import tech.maze.helloworld.backend.models.Message;
import tech.maze.helloworld.backend.services.MessagesService;

/**
 * A gRPC controller that handles requests for the Hello World service.
 */
@RequiredArgsConstructor
@GrpcService
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class HelloWorldGrpcController extends HelloWorldGrpc.HelloWorldImplBase {
  MessagesService messagesService;
  MessageMapper messageMapper;

  @Override
  public void add(
      AddRequest request,
      StreamObserver<AddResponse> responseObserver
  ) {
    final AddResponse response = AddResponse.newBuilder()
        .setMessage(messageMapper
            .toDto(messagesService
                .saveMessage(messageMapper
                    .toEntity(request))))
        .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void getAll(
      GetAllRequest request,
      StreamObserver<GetAllResponse> responseObserver
  ) {
    final GetAllResponse response = GetAllResponse.newBuilder()
        .addAllMessages(messagesService.getMessages()
          .stream()
          .map((Message message) -> messageMapper.toDto(message))
          .collect(Collectors.toList()))
        .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
