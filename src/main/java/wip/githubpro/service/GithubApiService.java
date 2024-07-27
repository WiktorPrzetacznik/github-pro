package wip.githubpro.service;

import reactor.core.publisher.Flux;
import wip.githubpro.model.github.Branch;
import wip.githubpro.model.github.Repository;

public interface GithubApiService {

    Flux<Repository> getUserRepositories(String username);

    Flux<Branch> getRepositoryBranches(String owner, String repo);

}