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
package org.springframework.xd.dirt.stream;

import org.springframework.xd.dirt.core.BaseDefinition;

/**
 * Represents the model for defining Triggers.
 *
 * @author Gunnar Hillert
 * @since 1.0
 *
 */
public class TriggerDefinition extends BaseDefinition {

	/**
	 * @param name The trigger name
	 * @param definition The trigger definition
	 *
	 */
	public TriggerDefinition(String name, String definition) {
		super(name, definition);
	}

	@Override
	public String toString() {
		return "TriggerDefinition [name=" + getName()
				+ ", definition=" + getDefinition()  + "]";
	}

}
