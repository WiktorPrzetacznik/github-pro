package wip.githubpro.service;

import wip.githubpro.model.repository.Branch;
import wip.githubpro.model.repository.Repository;

import java.util.List;

public interface GithubApiService {

    List<Repository> getUserRepositories(String username);

    List<Branch> getRepositoryBranches(String owner, String repo);

}