package wip.githubpro.service;

import wip.githubpro.model.github.Branch;
import wip.githubpro.model.github.Repository;

import java.util.List;

public interface GithubApiService {

    List<Repository> getUserRepositories(String username);

    List<Branch> getRepositoryBranches(String owner, String repo);

}