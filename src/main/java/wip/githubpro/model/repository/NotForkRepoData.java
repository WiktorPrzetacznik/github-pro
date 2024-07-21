package wip.githubpro.model.repository;

import wip.githubpro.model.github.Branch;

import java.util.List;

public record NotForkRepoData(String repositoryName, String ownerLogin, List<Branch> branches) {
}
