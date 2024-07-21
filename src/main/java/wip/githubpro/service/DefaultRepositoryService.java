package wip.githubpro.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilderFactory;
import wip.githubpro.model.NotForkRepoData;
import wip.githubpro.model.repository.Branch;
import wip.githubpro.model.repository.Repository;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DefaultRepositoryService implements RepositoryService {

    private final RestClient restClient;
    private final Pattern githubNextPageHeaderPattern;
    private final UriBuilderFactory githubUriBuilderFactory;

    public DefaultRepositoryService(RestClient restClient, Pattern githubNextPageHeaderPattern, UriBuilderFactory githubUriBuilderFactory) {
        this.restClient = restClient;
        this.githubNextPageHeaderPattern = githubNextPageHeaderPattern;
        this.githubUriBuilderFactory = githubUriBuilderFactory;
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
        return getData(
                githubUriBuilderFactory
                        .builder()
                        .path("/users/{username}/repos")
                        .build(username),
                new ParameterizedTypeReference<>(){}
        );
    }

    private List<Branch> getRepositoryBranches(String owner, String repo) {
        return getData(
                githubUriBuilderFactory
                        .builder()
                        .path("/repos/{owner}/{repo}/branches")
                        .build(owner, repo),
                new ParameterizedTypeReference<>(){}
        );
    }

    private <T> List<T> getData(URI uri, ParameterizedTypeReference<List<T>> ptr) {
        HttpEntity<List<T>> responseEntity = restClient.get()
                .uri(uri)
                .retrieve()
                .toEntity(ptr);
        List<T> data = responseEntity.getBody();
        HttpHeaders headers = responseEntity.getHeaders();
        if (headers.containsKey("link")) {
            getNextPaginationURI(headers.get("link"))
                    .ifPresent(u -> data.addAll(getData(u, ptr)));
        }
        return data;
    }

    private Optional<URI> getNextPaginationURI(List<String> linkHeaders) {
        if (!linkHeaders.isEmpty()) {
            for (String header : linkHeaders) {
                for (String processedHeader : header.split(",")) {
                    Matcher matcher = githubNextPageHeaderPattern.matcher(processedHeader);
                    if (matcher.find()) {
                        return Optional.of(URI.create(matcher.group(1)));
                    }
                }
            }
        }
        return Optional.empty();
    }

}