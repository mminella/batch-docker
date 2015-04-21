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
package io.spring.batch.domain;

/**
 * @author Michael Minella
 */
public class SearchResult {
	private String description;
	private boolean isAutomated;
	private boolean isOfficial;
	private boolean isTrusted;
	private String name;
	private int starCount;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isAutomated() {
		return isAutomated;
	}

	public void setIsAutomated(boolean isAutomated) {
		this.isAutomated = isAutomated;
	}

	public boolean isOfficial() {
		return isOfficial;
	}

	public void setIsOfficial(boolean isOfficial) {
		this.isOfficial = isOfficial;
	}

	public boolean isTrusted() {
		return isTrusted;
	}

	public void setIsTrusted(boolean isTrusted) {
		this.isTrusted = isTrusted;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStarCount() {
		return starCount;
	}

	public void setStarCount(int starCount) {
		this.starCount = starCount;
	}
}