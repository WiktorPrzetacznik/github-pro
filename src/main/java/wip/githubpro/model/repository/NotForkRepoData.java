package wip.githubpro.model.repository;

import wip.githubpro.model.github.Branch;

import java.util.List;

/**
 * @param repositoryName repository name
 * @param ownerLogin     username of the repository owner
 * @param branches       list of branches associated with the repository
 */
public record NotForkRepoData(String repositoryName, String ownerLogin, List<Branch> branches) {
}
