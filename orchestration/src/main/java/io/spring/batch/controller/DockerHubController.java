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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.pivotal.receptor.actions.RunAction;
import io.pivotal.receptor.client.ReceptorOperations;
import io.pivotal.receptor.commands.TaskCreateRequest;
import io.spring.batch.service.DockerHubTemplate;

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
public class DockerHubController {

	@Autowired
	private DockerHubTemplate service;

	@Autowired
	private ReceptorOperations receptorTemplate;

	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam("user") String user, Model model) {
		model.addAttribute("repositories", service.getRepositoriesByUser(user));

		return "docker/index";
	}

	@RequestMapping(value = "/launch", method = RequestMethod.POST)
	public String save(@RequestParam(value = "repositoryname", required = true) String repositoryName,
			@RequestParam(value = "tag", required = true) String tag, Model model) throws Exception {
		Map<String, RunAction> action = new HashMap<>();
		RunAction runAction = new RunAction();
		runAction.setPath("java");
		runAction.setArgs(new String[] {"-jar", "/app.jar"});
		runAction.setDir("/");
		action.put("run", runAction);

		TaskCreateRequest request = new TaskCreateRequest();
		request.setTaskGuid(UUID.randomUUID().toString());
		request.setDomain("batch");
		request.setStack("lucid64");
		request.setRootfs("docker:///" + repositoryName);
		request.setMemoryMb(512);
		request.setPrivileged(false);
		request.setLogGuid(request.getTaskGuid());
		request.setAction(action);

		receptorTemplate.createTask(request);

		return null;
	}
}
