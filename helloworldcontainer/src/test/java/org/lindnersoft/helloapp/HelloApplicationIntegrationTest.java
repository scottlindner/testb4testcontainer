package org.lindnersoft.helloapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class HelloApplicationIntegrationTest {

    private static final int CONTAINER_EXPOSED_PORT = 8080;
    private static final String TEST_MESSAGE = "What a lovely integration test.";

    private static File dockerfileFile = new File("Dockerfile");
    private static File jarFile = new File("./target/lib/helloapp-1.0-SNAPSHOT.jar");

    public static ImageFromDockerfile imageFromDockerfile = new ImageFromDockerfile()
            .withFileFromPath("/target/lib/helloapp-1.0-SNAPSHOT.jar", jarFile.toPath().toAbsolutePath())
            .withFileFromPath("Dockerfile", dockerfileFile.toPath().toAbsolutePath());

    @Container
    public static GenericContainer helloAppTestContainer
            = new GenericContainer(imageFromDockerfile)
            .withExposedPorts(CONTAINER_EXPOSED_PORT)
            .withEnv("MESSAGE", TEST_MESSAGE);

    @Test
    public void testHelloAppContainer() {
        String containerIpAddress = helloAppTestContainer.getContainerIpAddress();
        Integer containerPort = helloAppTestContainer.getFirstMappedPort();

        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://" + containerIpAddress + ":" + containerPort;
        ResponseEntity<String> response = restTemplate.getForEntity(resourceUrl, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), TEST_MESSAGE);
    }

    @Test
    public void fail() {
        assertTrue(false);
    }
}
