package wip.githubpro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;

import java.util.regex.Pattern;

@Configuration
public class GithubProConfiguration {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();
    }

    @Bean
    public Pattern githubNextPageHeaderPattern() {
        return Pattern.compile("<(.+)>; rel=\"next\"");
    }

    @Bean
    public UriBuilderFactory githubUriBuilderFactory() {
        return new DefaultUriBuilderFactory("https://api.github.com");
    }

}