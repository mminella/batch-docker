/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.dockerhub.client;

import java.util.List;

import io.pivotal.dockerhub.client.commands.SearchResults;
import io.pivotal.dockerhub.client.commands.Tag;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

/**
 * @author Michael Minella
 */
public class DockerHubTemplate implements DockerHubOperations {

	private RestOperations restTemplate;

	private static final String DEFAULT_DOCKER_HUB_HOST = "https://index.docker.io";

	protected static final ParameterizedTypeReference<List<Tag>> TAGS_LIST_TYPE = new ParameterizedTypeReference<List<Tag>>(){};

	private final String baseUrl;

	public DockerHubTemplate() {
		this(DEFAULT_DOCKER_HUB_HOST);
	}

	public DockerHubTemplate(String dockerHubHost) {
		this(dockerHubHost, new RestTemplate());
	}

	public DockerHubTemplate(String dockerHubHost, ClientHttpRequestFactory factory) {
		this(dockerHubHost, new RestTemplate(factory));
	}

	protected DockerHubTemplate(String dockerHubHost, RestOperations restTemplate) {
		this.baseUrl = (dockerHubHost.contains("://") ? dockerHubHost : "https://" + dockerHubHost) + "/v1";
		this.restTemplate = restTemplate;
	}

	@Override
	public SearchResults search(String searchString) {
		return restTemplate.getForEntity("{baseUrl}/search?q={user}", SearchResults.class, baseUrl, searchString).getBody();
	}

	@Override
	public List<Tag> getTags(String repositoryName) {
		return restTemplate.exchange("{baseUrl}/repositories/{name}/tags", HttpMethod.GET, null, TAGS_LIST_TYPE, baseUrl, repositoryName).getBody();
	}
}
