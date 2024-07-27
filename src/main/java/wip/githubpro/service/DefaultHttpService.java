package wip.githubpro.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DefaultHttpService implements HttpService {

    private final WebClient webClient;
    private final Pattern githubNextPageHeaderPattern;

    public DefaultHttpService(WebClient webClient, Pattern githubNextPageHeaderPattern) {
        this.webClient = webClient;
        this.githubNextPageHeaderPattern = githubNextPageHeaderPattern;
    }

    @Override
    public <T> Flux<T> getDataAsList(URI uri, ParameterizedTypeReference<List<T>> ptr) {
        return getData(uri, ptr)
                .expand(resp -> {
                    HttpHeaders headers = resp.getHeaders();
                    if (headers.containsKey("link")) {
                        Optional<URI> potentialURI = getNextPaginationURI(headers.get("link"));
                        if (potentialURI.isPresent()) {
                            return getData(potentialURI.get(), ptr);
                        }
                    }
                    return Mono.empty();
                })
                .flatMap(resp -> Flux.fromIterable(Objects.requireNonNull(resp.getBody())));
    }

    /**
     * @param uri URI leading to data
     * @param ptr response body type
     * @param <T> type of single data instance
     * @return Mono of data response
     */
    private <T> Mono<ResponseEntity<List<T>>> getData(URI uri, ParameterizedTypeReference<List<T>> ptr) {
        return webClient.get()
                .uri(uri)
                .retrieve()
                .toEntity(ptr);
    }

    /**
     * @param linkHeaders values associated with Link header
     * @return potential URI created from next pagination address
     */
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