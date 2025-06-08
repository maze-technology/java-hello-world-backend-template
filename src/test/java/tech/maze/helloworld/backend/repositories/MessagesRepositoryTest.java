/* File: src/test/java/tech/maze/helloworld/backend/repositories/MessagesRepositoryTest.java */
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

/**
 * Data-layer tests for {@link MessagesRepository}.
 */
@DataJpaTest(
  properties = {
    "spring.flyway.enabled=false",            // 🔑 don’t run Flyway in this slice
    "spring.jpa.hibernate.ddl-auto=create-drop" // optional: let Hibernate manage schema
  }
)
@EntityScan(basePackageClasses = Message.class)
@EnableJpaAuditing
class MessagesRepositoryTest {
  @Autowired
  MessagesRepository messagesRepository;

  @Test
  @DisplayName("save() should persist a Message, populate id and createdAt")
  void save_shouldPersistEntity() {
    Message saved = messagesRepository.save(
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
    Message saved = messagesRepository.save(
      Message.builder().content("bonjour").build()
    );

    assertThat(messagesRepository.findById(saved.getId())).contains(saved);
  }

  @Test
  @DisplayName("findAll() should return every persisted Message")
  void findAll_shouldReturnList() {
    Message first = messagesRepository.save(
      Message.builder().content("one").build()
    );
    Message second = messagesRepository.save(
      Message.builder().content("two").build()
    );

    List<Message> all = messagesRepository.findAll();

    assertThat(all).containsExactlyInAnyOrder(first, second);
  }

  @Test
  @DisplayName("deleteById() should remove the entity")
  void deleteById_shouldRemove() {
    Message saved = messagesRepository.save(
      Message.builder().content("bye").build()
    );

    messagesRepository.deleteById(saved.getId());

    assertThat(messagesRepository.existsById(saved.getId())).isFalse();
  }

  @Test
  @DisplayName("deleteById() on an unknown id should do nothing and not throw")
  void deleteById_unknownId_shouldNotThrow() {
    UUID unknown = UUID.randomUUID();
    long countBefore = messagesRepository.count();

    // Spring Data JPA 3.x no longer raises an exception here
    assertThatCode(() -> messagesRepository.deleteById(unknown))
      .doesNotThrowAnyException();

    assertThat(messagesRepository.count()).isEqualTo(countBefore);
  }

  @Test
  @DisplayName("existsById() reflects repository state")
  void existsById_shouldReturnCorrectValue() {
    Message saved = messagesRepository.save(
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
