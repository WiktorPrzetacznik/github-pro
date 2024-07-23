package wip.githubpro.model.github;

/**
 * @param name   name of this branch
 * @param commit last commit on this branch
 */
public record Branch(String name, Commit commit) {
}