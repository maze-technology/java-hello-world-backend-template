package tech.maze.service.template;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootTest
@ActiveProfiles("test")
@Disabled
public class AppTest {
  @Test
  public void testContainer() throws IOException, InterruptedException {
    try (final GenericContainer<?> container = new GenericContainer<>("nginx").withExposedPorts(80)) {
      container.start();

      final HttpClient httpClient = HttpClient.newHttpClient();
      final String uri = "http://" + container.getHost() + ":" + container.getFirstMappedPort();

      final HttpRequest request = HttpRequest.newBuilder(URI.create(uri))
        .GET()
        .build();

      Thread.sleep(3000);

      final HttpResponse<String> response = httpClient.send(
        request,
        HttpResponse.BodyHandlers.ofString()
      );

      Assertions.assertTrue(response.body().contains("Thank you for using nginx."));
    }
  }
}
