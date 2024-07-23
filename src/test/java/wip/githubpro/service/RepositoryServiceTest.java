package wip.githubpro.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.util.UriBuilderFactory;
import wip.githubpro.model.github.Branch;
import wip.githubpro.model.github.Commit;
import wip.githubpro.model.github.Owner;
import wip.githubpro.model.github.Repository;
import wip.githubpro.model.repository.NotForkRepoData;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RepositoryServiceTest {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private UriBuilderFactory githubUriBuilderFactory;
    @MockBean
    private HttpService httpService;

    @Test
    public void testListNotForks() {
        URI getReposURI = githubUriBuilderFactory
                .builder()
                .path("/users/{username}/repos")
                .build("username");
        URI getBranchesURI = githubUriBuilderFactory
                .builder()
                .path("/repos/{owner}/{repo}/branches")
                .build("owner", "repo1");

        when(httpService.getDataAsList(getReposURI, new ParameterizedTypeReference<List<Repository>>() {
        }))
                .thenReturn(List.of(
                        new Repository("repo1", new Owner("owner"), false),
                        new Repository("repo2", new Owner("owner"), true)
                ));
        when(httpService.getDataAsList(getBranchesURI, new ParameterizedTypeReference<List<Branch>>() {
        }))
                .thenReturn(List.of(
                        new Branch("branch1", new Commit("h4hm49mh946hm4"))
                ));

        List<NotForkRepoData> res = repositoryService.listNotForks("username");
        List<NotForkRepoData> expectedRes = List.of(
                new NotForkRepoData("repo1", "owner", List.of(
                        new Branch("branch1", new Commit("h4hm49mh946hm4"))
                ))
        );

        assertThat(res).isEqualTo(expectedRes);
    }
}