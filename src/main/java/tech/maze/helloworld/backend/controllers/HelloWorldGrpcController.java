package tech.maze.helloworld.backend.controllers;

import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
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
@GrpcAdvice
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class HelloWorldGrpcController extends HelloWorldGrpc.HelloWorldImplBase {
  MessagesService messagesService;
  MessageMapper messageMapper;

  @Override
  public void add(
      AddRequest request,
      StreamObserver<AddResponse> responseObserver
  ) {
    // TODO: Add protovalidate validation
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
    // TODO: Add protovalidate validation
    final GetAllResponse response = GetAllResponse.newBuilder()
        .addAllMessages(messagesService.getMessages()
          .stream()
          .map((Message message) -> messageMapper.toDto(message))
          .collect(Collectors.toList()))
        .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  /**
   * Handle ConstraintViolationException raised by validation and pipe it to GRPC.
   */
  @GrpcExceptionHandler(ConstraintViolationException.class)
  public StatusException handleConstraintViolationException(ConstraintViolationException e) {
    Status status = Status.INVALID_ARGUMENT.withDescription(e.getMessage()).withCause(e);

    return status.asException();
  }

  /**
   * Handle IllegalArgumentException raised and pipe it to GRPC.
   */
  @GrpcExceptionHandler(IllegalArgumentException.class)
  public StatusException handleIllegalArgumentException(IllegalArgumentException e) {
    Status status = Status.INVALID_ARGUMENT.withDescription(e.getMessage()).withCause(e);

    return status.asException();
  }
}
