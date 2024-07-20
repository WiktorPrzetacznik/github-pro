package wip.githubpro.model;

import wip.githubpro.model.repository.Branch;

import java.util.List;

public record NotForkRepoData(String repositoryName, String ownerLogin, List<Branch> branches) {
}
