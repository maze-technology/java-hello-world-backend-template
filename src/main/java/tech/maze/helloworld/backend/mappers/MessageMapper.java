package tech.maze.helloworld.backend.mappers;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import tech.maze.commons.mappers.BaseDtoMapper;
import tech.maze.commons.mappers.UuidMapper;
import tech.maze.helloworld.backend.models.Message;

/**
 * Mapper for converting between Message entity and its corresponding DTO model.
 */
@Mapper(
    componentModel = "spring",
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {
      BaseDtoMapper.class,
      UuidMapper.class
    }
)
public abstract class MessageMapper {
  public static final MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

  /**
   * Maps a Message entity to its corresponding DTO model.
   *
   * @param messageEntity the Message entity to convert
   * @return the corresponding Message DTO
   */
  public abstract tech.maze.dtos.helloworld.models.Message toDto(Message messageEntity);

  /**
   * Maps a Message DTO to a Message entity.
   *
   * @param messageDto the Message DTO to convert
   * @return the corresponding Message entity
   */
  public abstract Message toEntity(tech.maze.dtos.helloworld.models.Message messageDto);

  /**
   * Maps an AddRequest DTO to a Message entity.
   *
   * @param addRequestDto the request DTO to convert
   * @return the corresponding Message entity
   */
  public abstract Message toEntity(tech.maze.dtos.helloworld.payloads.AddRequest addRequestDto);
}
