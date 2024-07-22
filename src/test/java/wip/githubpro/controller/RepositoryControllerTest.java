package wip.githubpro.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriBuilderFactory;
import wip.githubpro.model.github.Branch;
import wip.githubpro.model.github.Commit;
import wip.githubpro.model.github.Owner;
import wip.githubpro.model.github.Repository;
import wip.githubpro.service.HttpService;

import java.net.URI;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RepositoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UriBuilderFactory githubUriBuilderFactory;

    @MockBean
    private HttpService httpService;

    @Test
    public void statusIsOk() throws Exception {
        when(httpService.getDataAsList(getReposURI(), new ParameterizedTypeReference<List<Repository>>() {}))
                .thenReturn(List.of(
                        new Repository("repo1", new Owner("owner"), false),
                        new Repository("repo2", new Owner("owner"), true)
                ));
        when(httpService.getDataAsList(getBranchesURI(), new ParameterizedTypeReference<List<Branch>>() {}))
                .thenReturn(List.of(
                        new Branch("branch1", new Commit("h4hm49mh946hm4"))
                ));

        mockMvc.perform(get("/user/username"))
                .andExpect(status().isOk());
    }

    @Test
    public void statusIsNotFound() throws Exception {
        when(httpService.getDataAsList(getReposURI(), new ParameterizedTypeReference<List<Repository>>() {}))
                .thenThrow(new HttpClientErrorException(HttpStatusCode.valueOf(404), "Not Found"));

        mockMvc.perform(get("/user/username"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void okContentIsJson() throws Exception {
        when(httpService.getDataAsList(getReposURI(), new ParameterizedTypeReference<List<Repository>>() {}))
                .thenReturn(List.of(
                        new Repository("repo1", new Owner("owner"), false),
                        new Repository("repo2", new Owner("owner"), true)
                ));
        when(httpService.getDataAsList(getBranchesURI(), new ParameterizedTypeReference<List<Branch>>() {}))
                .thenReturn(List.of(
                        new Branch("branch1", new Commit("h4hm49mh946hm4"))
                ));

        mockMvc.perform(get("/user/username"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void notFoundContentIsJson() throws Exception {
        when(httpService.getDataAsList(getReposURI(), new ParameterizedTypeReference<List<Repository>>() {}))
                .thenThrow(new HttpClientErrorException(HttpStatusCode.valueOf(404), "Not Found"));

        mockMvc.perform(get("/user/username"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    private URI getReposURI() {
        return githubUriBuilderFactory
                .builder()
                .path("/users/{username}/repos")
                .build("username");
    }

    private URI getBranchesURI() {
        return githubUriBuilderFactory
                .builder()
                .path("/repos/{owner}/{repo}/branches")
                .build("owner", "repo1");
    }

}
