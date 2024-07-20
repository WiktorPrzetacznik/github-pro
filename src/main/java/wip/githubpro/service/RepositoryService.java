package wip.githubpro.service;

import wip.githubpro.model.NotForkRepoData;

import java.util.List;

public interface RepositoryService {

    List<NotForkRepoData> listNotForks(String username);

}