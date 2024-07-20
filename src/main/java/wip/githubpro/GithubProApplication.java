package wip.githubpro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class GithubProApplication {

    public static void main(String[] args) {
        SpringApplication.run(GithubProApplication.class, args);
    }

}
