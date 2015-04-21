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
package io.spring.batch.service;

import java.util.ArrayList;
import java.util.List;

import io.spring.batch.domain.Repository;
import io.spring.batch.domain.SearchResult;
import io.spring.batch.domain.SearchResults;
import io.spring.batch.domain.Tag;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

/**
 * @author Michael Minella
 */
@Service
public class DockerHubTemplate {

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

	public List<Repository> getRepositoriesByUser(String user) {
		SearchResults results =
				restTemplate.getForEntity("{baseUrl}/search?q={user}", SearchResults.class, baseUrl, user).getBody();

		List<Repository> repositories = new ArrayList<>();

		for (SearchResult searchResult : results.getResults()) {
			List<Tag> tags =
					restTemplate.exchange("{baseUrl}/repositories/{name}/tags", HttpMethod.GET, null, TAGS_LIST_TYPE, baseUrl, searchResult.getName()).getBody();

			repositories.add(new Repository(searchResult.getName(), searchResult.getDescription(), tags));
		}

		return repositories;
	}
}
