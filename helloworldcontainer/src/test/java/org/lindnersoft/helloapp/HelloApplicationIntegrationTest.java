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
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class HelloApplicationIntegrationTest {

    private static final int port = 8080;

    // Test data
    private URI targetUri;


    private static File dockerfileFile = new File("Dockerfile");
    private static String dockerFilePath = dockerfileFile.getAbsolutePath();

    public static ImageFromDockerfile imageFromDockerfile = new ImageFromDockerfile()
            .withFileFromFile("Dockerfile", dockerfileFile);

    private static String dockerImageName = "helloworldcontainer:1.0-SNAPSHOT";

    @Container
    public static GenericContainer helloAppTestContainer
            = new GenericContainer(dockerImageName)
            .withExposedPorts(port)
            .withEnv("MESSAGE", "Hello from the integration test.");

    @BeforeEach
    public void beforeEach() {
    }

    @Test
    public void remoteProtoRequest() {
        String containerIpAddress = helloAppTestContainer.getContainerIpAddress();
        Integer containerPort = helloAppTestContainer.getFirstMappedPort();

        targetUri = URI.create(containerIpAddress + ":" + containerPort);

        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://" + targetUri.toString();
        ResponseEntity<String> response = restTemplate.getForEntity(resourceUrl, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        assertTrue(true);
    }
}
