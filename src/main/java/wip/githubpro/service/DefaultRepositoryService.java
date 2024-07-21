package wip.githubpro.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import wip.githubpro.model.repository.NotForkRepoData;

import java.util.List;

@Service
public class DefaultRepositoryService implements RepositoryService {

    private final GithubApiService githubApiService;

    public DefaultRepositoryService(GithubApiService githubApiService) {
        this.githubApiService = githubApiService;
    }

    @Override
    @Cacheable(cacheNames = "notforks", key = "#username")
    public List<NotForkRepoData> listNotForks(String username) {
        return githubApiService.getUserRepositories(username)
                .stream()
                .filter(repo -> !repo.fork())
                .map(repo -> new NotForkRepoData(
                        repo.name(), repo.owner().login(),
                        githubApiService.getRepositoryBranches(repo.owner().login(), repo.name())
                ))
                .toList();
    }

}