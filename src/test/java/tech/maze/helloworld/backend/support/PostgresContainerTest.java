package tech.maze.helloworld.backend.support;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Reusable PostgreSQL 16 Testcontainer.
 * {@code @ServiceConnection} lets Spring Boot wire the DataSource, Flyway, etc.
 */
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class PostgresContainerTest {

  @SuppressWarnings("resource") // INFO: False positive, Testcontainers closes it for us
  @Container
  @ServiceConnection
  protected static final PostgreSQLContainer<?> POSTGRES =
      new PostgreSQLContainer<>("postgres:16-alpine").withReuse(true);
}
