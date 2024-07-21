package wip.githubpro.service;

import org.springframework.core.ParameterizedTypeReference;

import java.net.URI;
import java.util.List;

public interface HttpService {

    <T> List<T> getDataAsList(URI uri, ParameterizedTypeReference<List<T>> ptr);

}