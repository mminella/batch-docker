package io.pivotal.dockerhub.client;

import java.util.List;

import io.pivotal.dockerhub.client.commands.SearchResults;
import io.pivotal.dockerhub.client.commands.Tag;

/**
 * @author Michael Minella
 */
public interface DockerHubOperations {
	SearchResults search(String searchString);

	List<Tag> getTags(String repositoryName);
}
