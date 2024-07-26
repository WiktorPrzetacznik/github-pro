package wip.githubpro.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import wiremock.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest
@WireMockTest
public class RepositoryControllerTest {

    private WebTestClient client;
    private WireMockServer wireMockServer;

    @Value("${server.port}")
    private String port;

    @BeforeEach
    public void setUp() throws IOException {
        setUpWireMockServer();
        setUpClient();
    }

    @AfterEach
    public void clear() {
        wireMockServer.stop();
    }

    private void setUpWireMockServer() throws IOException {
        wireMockServer = new WireMockServer();
        configureFor("localhost", Integer.parseInt(port));
        wireMockServer.start();
        stubFor(get(urlEqualTo("/user/existing_user"))
                .willReturn(
                        aResponse().withStatus(200)
                                .withBody(IOUtils.resourceToString("/responses/existing_user_response.json", StandardCharsets.UTF_8))
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                ));
        stubFor(get(urlEqualTo("/user/non_existing_user"))
                .willReturn(
                        aResponse().withStatus(404)
                                .withBody(IOUtils.resourceToString("/responses/non_existing_user_response.json", StandardCharsets.UTF_8))
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                ));
    }

    private void setUpClient() {
        client = WebTestClient
                .bindToServer()
                .baseUrl(String.format("http://localhost:%s", port))
                .build();
    }

    @Test
    public void listNonForks_statusIsOk() {
        client.get().uri("/user/existing_user")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void listNonForks_statusIsNotFound() {
        client.get().uri("/user/non_existing_user")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void listNonForks_okIsJson() {
        client.get().uri("/user/existing_user")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void listNonForks_NotFoundIsJson() {
        client.get().uri("/user/non_existing_user")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

}