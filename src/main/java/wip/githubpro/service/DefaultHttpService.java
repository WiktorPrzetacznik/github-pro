package wip.githubpro.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DefaultHttpService implements HttpService {

    private final RestClient restClient;
    private final Pattern githubNextPageHeaderPattern;

    public DefaultHttpService(RestClient restClient, Pattern githubNextPageHeaderPattern) {
        this.restClient = restClient;
        this.githubNextPageHeaderPattern = githubNextPageHeaderPattern;
    }

    @Override
    public <T> List<T> getDataAsList(URI uri, ParameterizedTypeReference<List<T>> ptr) {
        HttpEntity<List<T>> responseEntity = restClient.get()
                .uri(uri)
                .retrieve()
                .toEntity(ptr);
        List<T> data = responseEntity.getBody();
        HttpHeaders headers = responseEntity.getHeaders();
        if (headers.containsKey("link")) {
            getNextPaginationURI(headers.get("link"))
                    .ifPresent(u -> data.addAll(getDataAsList(u, ptr)));
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