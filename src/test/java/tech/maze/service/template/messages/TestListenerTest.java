package tech.maze.service.template.messages;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Disabled
class TestListenerTest {
  private static final DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack:latest");

  @Container
  private static final LocalStackContainer localStack = new LocalStackContainer(LOCALSTACK_IMAGE)
    .withServices(SQS);

  @BeforeEach
  void setUp() {
    if (!localStack.isRunning()) {
      localStack.start();
    }
  }

  @Test
  void listen() {
    Assertions.assertThat(localStack.isRunning()).isTrue();
  }

  @AfterEach
  void tearDown() {
    if (localStack.isRunning()) {
      localStack.stop();
    }
  }
}
