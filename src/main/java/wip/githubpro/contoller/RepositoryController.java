package wip.githubpro.contoller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import wip.githubpro.model.NotForkRepoData;
import wip.githubpro.service.RepositoryService;

import java.util.List;

@RestController
public class RepositoryController {

    private final RepositoryService repositoryService;

    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @GetMapping(value = "/user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NotForkRepoData> listNotForks(@PathVariable("username") String username) {
        return repositoryService.listNotForks(username);
    }

}