package wip.githubpro.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriBuilderFactory;
import reactor.core.publisher.Flux;
import wip.githubpro.model.github.Branch;
import wip.githubpro.model.github.Repository;

@Service
public class DefaultGithubApiService implements GithubApiService {

    private final HttpService httpService;
    private final UriBuilderFactory githubUriBuilderFactory;

    public DefaultGithubApiService(HttpService httpService, UriBuilderFactory githubUriBuilderFactory) {
        this.httpService = httpService;
        this.githubUriBuilderFactory = githubUriBuilderFactory;
    }

    @Override
    public Flux<Repository> getUserRepositories(String username) {
        return httpService.getDataAsList(
                githubUriBuilderFactory
                        .builder()
                        .path("/users/{username}/repos")
                        .build(username),
                new ParameterizedTypeReference<>() {
                }
        );
    }

    @Override
    public Flux<Branch> getRepositoryBranches(String owner, String repo) {
        return httpService.getDataAsList(
                githubUriBuilderFactory
                        .builder()
                        .path("/repos/{owner}/{repo}/branches")
                        .build(owner, repo),
                new ParameterizedTypeReference<>() {
                }
        );
    }

}