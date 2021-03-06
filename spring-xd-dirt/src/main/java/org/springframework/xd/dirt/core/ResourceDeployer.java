/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.springframework.xd.dirt.core;


/**
 * Interface for XD Resource Services.
 *
 * @param <R> the kind of resource to deploy (<i>e.g.</i> Stream)
 * @author David Turanski
 * @author Gunnar Hillert
 *
 */
public interface ResourceDeployer<R extends BaseDefinition> {

	void deploy(String name);

	/**
	 * Return all definitions ordered by {@link BaseDefinition#getName()} ascending.
	 */
	Iterable<R> findAll();

	R save(R resource);

	/**
	 * Retrieves a single Definition or null if none is found.
	 *
	 * @param name of the definition to find. Must not be null.
	 */
	R findOne(String name);


	/**
	 * Delete the Definition using the provided name.
	 *
	 * @param name Must not be null.
	 */
	void delete(String name);

	void undeploy(String name);
}
