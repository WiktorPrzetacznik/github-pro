package wip.githubpro.model.github;

/**
 * @param name  repository name
 * @param owner owner associated with this repository
 * @param fork  flag informing if this repository is a fork
 */
public record Repository(String name, Owner owner, boolean fork) {
}