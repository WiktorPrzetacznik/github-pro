package wip.githubpro.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import wip.githubpro.model.NotForkRepoData;
import wip.githubpro.model.repository.Branch;
import wip.githubpro.model.repository.Repository;

import java.util.List;

@Service
public class DefaultRepositoryService implements RepositoryService {

    private final RestClient restClient;

    public DefaultRepositoryService(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    @Cacheable(cacheNames = "notforks", key = "#username")
    public List<NotForkRepoData> listNotForks(String username) {
        return getUserRepositories(username)
                .stream()
                .filter(repo -> !repo.fork())
                .map(repo -> new NotForkRepoData(
                        repo.name(), repo.owner().login(), getRepositoryBranches(repo.owner().login(), repo.name())
                ))
                .toList();
    }

    private List<Repository> getUserRepositories(String username) {
        return restClient.get()
                .uri(b -> b.path("/users/{username}/repos").build(username))
                .retrieve()
                .body(new ParameterizedTypeReference<>(){});
    }

    private List<Branch> getRepositoryBranches(String owner, String repo) {
        return restClient.get()
                .uri(b -> b.path("/repos/{owner}/{repo}/branches").build(owner, repo))
                .retrieve()
                .body(new ParameterizedTypeReference<>(){});
    }

}