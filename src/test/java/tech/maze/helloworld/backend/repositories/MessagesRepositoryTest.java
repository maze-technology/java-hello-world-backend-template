package tech.maze.helloworld.backend.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import tech.maze.helloworld.backend.models.Message;
import tech.maze.helloworld.backend.support.PostgresContainerTest;  // ⬅ shared container

/**
 * Data-layer tests for {@link MessagesRepository} against PostgreSQL Testcontainer.
 */
@DataJpaTest
@EntityScan(basePackageClasses = Message.class)
@EnableJpaAuditing
class MessagesRepositoryTest extends PostgresContainerTest {
  @Autowired
  MessagesRepository messagesRepository;

  @Test
  @DisplayName("save() should persist a Message, populate id and createdAt")
  void save_shouldPersistEntity() {
    final Message saved = messagesRepository.save(
      Message.builder().content("hello world").build()
    );

    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getCreatedAt()).isNotNull();
    assertThat(saved.getContent()).isEqualTo("hello world");
    assertThat(messagesRepository.count()).isEqualTo(1);
  }

  @Test
  @DisplayName("findById() should return the persisted entity")
  void findById_shouldReturnEntity() {
    final Message saved = messagesRepository.save(
      Message.builder().content("bonjour").build()
    );

    assertThat(messagesRepository.findById(saved.getId())).contains(saved);
  }

  @Test
  @DisplayName("findAll() should return every persisted Message")
  void findAll_shouldReturnList() {
    final Message first  = messagesRepository.save(Message.builder().content("one").build());
    final Message second = messagesRepository.save(Message.builder().content("two").build());

    final List<Message> all = messagesRepository.findAll();
    assertThat(all).containsExactlyInAnyOrder(first, second);
  }

  @Test
  @DisplayName("deleteById() should remove the entity")
  void deleteById_shouldRemove() {
    final Message saved = messagesRepository.save(
      Message.builder().content("bye").build()
    );

    messagesRepository.deleteById(saved.getId());
    assertThat(messagesRepository.existsById(saved.getId())).isFalse();
  }

  @Test
  @DisplayName("deleteById() on an unknown id should do nothing and not throw")
  void deleteById_unknownId_shouldNotThrow() {
    final long before = messagesRepository.count();
    assertThatCode(() -> messagesRepository.deleteById(UUID.randomUUID()))
        .doesNotThrowAnyException();
    assertThat(messagesRepository.count()).isEqualTo(before);
  }

  @Test
  @DisplayName("existsById() reflects repository state")
  void existsById_shouldReturnCorrectValue() {
    final Message saved = messagesRepository.save(
      Message.builder().content("exists?").build()
    );

    assertThat(messagesRepository.existsById(saved.getId())).isTrue();
    messagesRepository.delete(saved);
    assertThat(messagesRepository.existsById(saved.getId())).isFalse();
  }

  @Test
  @DisplayName("count() returns the number of rows")
  void count_shouldReturnRowCount() {
    assertThat(messagesRepository.count()).isZero();
    messagesRepository.save(Message.builder().content("alpha").build());
    messagesRepository.save(Message.builder().content("beta").build());
    assertThat(messagesRepository.count()).isEqualTo(2);
  }
}
