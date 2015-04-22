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
package io.spring.batch.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.pivotal.dockerhub.client.DockerHubOperations;
import io.pivotal.dockerhub.client.commands.SearchResults;
import io.pivotal.dockerhub.client.commands.Tag;
import io.pivotal.receptor.actions.RunAction;
import io.pivotal.receptor.client.ReceptorOperations;
import io.pivotal.receptor.commands.TaskCreateRequest;
import io.spring.batch.domain.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Michael Minella
 */
@Controller
@RequestMapping("/docker")
public class LaunchingController {

	@Autowired
	private DockerHubOperations dockerHubTemplate;

	@Autowired
	private ReceptorOperations receptorTemplate;

	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam("user") String user, Model model) {
		SearchResults searchResults = dockerHubTemplate.search(user);

		List<Repository> repositories = new ArrayList<>();
		for (SearchResults.SearchResult searchResult : searchResults.getResults()) {
			List<Tag> tags = dockerHubTemplate.getTags(searchResult.getName());

			Repository repository = new Repository(searchResult.getName(), searchResult.getDescription(), tags);

			repositories.add(repository);
		}

		model.addAttribute("repositories", repositories);

		return "docker/index";
	}

	@RequestMapping(value = "/launch", method = RequestMethod.POST)
	public String save(@RequestParam(value = "repositoryName", required = true) String repositoryName,
			@RequestParam(value = "tag", required = true) String tag, @RequestParam("user") String user,
			Model model) throws Exception {
		Map<String, RunAction> action = new HashMap<>();
		RunAction runAction = new RunAction();
		runAction.setPath("java");
		runAction.setArgs(new String[] {"-jar", "/app.jar"});
		runAction.setDir("/");
		action.put("run", runAction);

		TaskCreateRequest request = new TaskCreateRequest();
		request.setTaskGuid(UUID.randomUUID().toString());
		request.setDomain("batch");
		request.setRootfs("docker:///" + repositoryName + "#" + tag);
		request.setMemoryMb(512);
		request.setPrivileged(false);
		request.setLogGuid(request.getTaskGuid());
		request.setAction(action);
		TaskCreateRequest.EgressRule rule = new TaskCreateRequest.EgressRule();
		rule.setProtocol("all");
		// This IP address is the IP for smtp.gmail.com
		rule.setDestinations(new String[] {"74.125.202.108"});
		TaskCreateRequest.PortRange portRange = new TaskCreateRequest.PortRange();
		portRange.setStart(587);
		portRange.setEnd(587);
		rule.setPortRange(portRange);
		request.setEgressRules(new TaskCreateRequest.EgressRule[] {rule});

		receptorTemplate.createTask(request);

		model.addAttribute("repositorySubmitted", repositoryName + "#" + tag);

		return "redirect:/status?repositorySubmitted=" + repositoryName + "#" + tag;
	}
}
