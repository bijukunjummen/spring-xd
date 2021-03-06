/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.xd.shell.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;
import org.springframework.xd.rest.client.AggregateCounterOperations;
import org.springframework.xd.shell.XDShell;

/**
 * Commands for interacting with aggregate counter analytics.
 *
 * @author Ilayaperumal Gopinathan
 */
@Component
public class AggregateCounterCommands extends AbstractMetricsCommands implements CommandMarker {

	protected AggregateCounterCommands() {
		super("AggregateCounter");
	}

	private static final String DISPLAY_AGGR_COUNTER = "aggregatecounter display";

	private static final String LIST_AGGR_COUNTERS = "aggregatecounter list";

	private static final String DELETE_AGGR_COUNTER = "aggregatecounter delete";

	@Autowired
	private XDShell xdShell;

	@CliAvailabilityIndicator({ DELETE_AGGR_COUNTER })
	public boolean available() {
		return xdShell.getSpringXDOperations() != null;
	}
	
	@CliCommand(value = DELETE_AGGR_COUNTER, help= "Delete the aggregate counter")
	public String delete(
			@CliOption(key = {"", "name"}, help = "the name of the aggregate counter to delete", mandatory = true) String name) {
		aggrCounterOperations().delete(name);
		return String.format("Deleted aggregate counter '%s'", name);
	}

	private AggregateCounterOperations aggrCounterOperations() {
		return xdShell.getSpringXDOperations().aggrCounterOperations();
	}

}
