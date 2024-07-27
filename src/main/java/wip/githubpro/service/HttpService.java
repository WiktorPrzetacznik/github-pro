package wip.githubpro.service;

import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.List;

public interface HttpService {

    <T> Flux<T> getDataAsList(URI uri, ParameterizedTypeReference<List<T>> ptr);

}