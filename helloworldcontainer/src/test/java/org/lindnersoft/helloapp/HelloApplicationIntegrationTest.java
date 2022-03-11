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

@Testcontainers
public class HelloApplicationIntegrationTest {

    private static final int CONTAINER_EXPOSED_PORT = 8080;
    private static final String TEST_MESSAGE = "What a lovely integration test.";

    private static File dockerfileFile = new File("Dockerfile");
    private static String dockerfileFileAbsolutePath = dockerfileFile.getAbsolutePath();

    public static ImageFromDockerfile imageFromDockerfile = new ImageFromDockerfile()
            .withFileFromPath("Dockerfile", dockerfileFile.toPath());

    private static String dockerImageName = "helloworldcontainer:1.0-SNAPSHOT";

    @Container
    public static GenericContainer helloAppTestContainer
            = new GenericContainer(dockerImageName)
            .withExposedPorts(CONTAINER_EXPOSED_PORT)
            .withEnv("MESSAGE", TEST_MESSAGE);

//    @Container
//    public static GenericContainer helloAppTestContainer2
//            = new GenericContainer(imageFromDockerfile)
//            .withExposedPorts(CONTAINER_EXPOSED_PORT)
//            .withEnv("MESSAGE", TEST_MESSAGE);

    @BeforeEach
    public void beforeEach() {
    }

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
}
