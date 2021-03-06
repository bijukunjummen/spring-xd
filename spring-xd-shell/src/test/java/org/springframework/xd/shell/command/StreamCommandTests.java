/*
 * Copyright 2013 the original author or authors.
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
package org.springframework.xd.shell.command;

import static org.junit.Assert.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.shell.core.CommandResult;

/**
 * Test stream commands
 *
 * @author Mark Pollack
 * @author Kashyap Parikh
 * @author Andy Clement
 */
public class StreamCommandTests extends AbstractStreamIntegrationTest {

	private static final Log logger = LogFactory
			.getLog(StreamCommandTests.class);

	@Test
	public void testStreamLifecycleForTickTock() throws InterruptedException {
		logger.info("Starting Stream Test for TickTock");
		String streamName = "ticktock";
		executeStreamCreate(streamName, "time | log");
		executeStreamUndeploy(streamName);
	}

	@Test
	public void testStreamCreateDuplicate() throws InterruptedException {
		logger.info("Create tictock stream");
		String streamName = "ticktock";
		String streamDefinition = "time | log";
		executeStreamCreate(streamName, streamDefinition);

		CommandResult cr = getShell().executeCommand(
				"stream create --definition \""+streamDefinition+"\" --name ticktock");
		assertTrue("Failure.  CommandResult = " + cr.toString(),
				!cr.isSuccess());
		assertTrue(
				"Failure.  CommandResult = " + cr.toString(),
				cr.getException().getMessage()
						.contains("There is already a stream named 'ticktock'"));
	}

	@Test
	public void testStreamDestroyMissing() {
		logger.info("Destroy a stream that doesn't exist");
		CommandResult cr = getShell().executeCommand(
				"stream destroy --name ticktock");
		assertTrue("Failure.  CommandResult = " + cr.toString(),
				!cr.isSuccess());
		assertTrue(
				"Failure.  CommandResult = " + cr.toString(),
				cr.getException()
						.getMessage()
						.contains(
								"Can't delete stream 'ticktock' because it does not exist"));
	}

	@Test
	public void testStreamCreateDuplicateWithDeployFalse() {
		logger.info("Create 2 tictok streams with --deploy = false");
		String streamName = "ticktock";
		String streamDefinition = "time | log";
		executeStreamCreate(streamName, streamDefinition, false);

		CommandResult cr = getShell().executeCommand(
						"stream create --definition \""+streamDefinition+"\" --name "+streamName+" --deploy false");
		assertTrue("Failure.  CommandResult = " + cr.toString(),
				!cr.isSuccess());
		assertTrue(
				"Failure.  CommandResult = " + cr.toString(),
				cr.getException().getMessage()
						.contains("There is already a stream named 'ticktock'"));

		verifyStreamExists(streamName, streamDefinition);
	}

	@Test
	public void testStreamDeployUndeployFlow() {
		logger.info("Create tictok stream");
		String streamName = "ticktock";
		String streamDefinition = "time | log";
		executeStreamCreate(streamName, streamDefinition, false);

		executeStreamDeploy(streamName);
		verifyStreamExists(streamName, streamDefinition);

		executeStreamUndeploy(streamName);
		verifyStreamExists(streamName, streamDefinition);

		executeStreamDeploy(streamName);
		verifyStreamExists(streamName, streamDefinition);

	}

	// This test hangs the server (produces error: dispatcher has no subscribers for channel 'foox')
	@Ignore
	@Test
	public void testNamedChannelSyntax() {
		logger.info("Create ticktock stream");
		executeStreamCreate("ticktock-in", "http --port=9314 > :foox", true);
		executeStreamCreate("ticktock-out", ":foo > log", true);

		executeCommand("http post --data blahblah --target http://localhost:9314");
	}

	@Test
	public void testNamedChannelsLinkingSourceAndSink() {
		executeStreamCreate("ticktock-in", "http --port=9314 > :foo", true);
		executeStreamCreate("ticktock-out",
				":foo > transform --expression=payload.toUpperCase() | log", true);
		executeCommand("http post --data blahblah --target http://localhost:9314");
	}

	@Test
	public void testDefiningSubstream() {
		executeStreamCreate("s1","transform --expression=payload.replace('Andy','zzz')",false);
	}

	@Test
	public void testUsingSubstream() {
		executeStreamCreate("s1","transform --expression=payload.replace('Andy','zzz')",false);
		executeStreamCreate("s2","http --port=9314 | s1 | log",true);

		executeCommand("http post --data fooAndyfoo --target http://localhost:9314");
	}

	@Test
	public void testUsingSubstreamWithParameterizationAndDefaultValue() {
		executeStreamCreate("obfuscate","transform --expression=payload.replace('${text:rys}','.')",false);
		executeStreamCreate("s2","http --port=9314 | obfuscate | log",true);
		executeCommand("http post --data Dracarys! --target http://localhost:9314");
		// TODO verify the output of the 'log' sink is 'Draca.!'
	}

	@Test
	public void testUsingSubstreamWithParameterization() {
		executeStreamCreate("obfuscate","transform --expression=payload.replace('${text}','.')",false);
		executeStreamCreate("s2","http --port=9314 | obfuscate --text=aca | log",true);
		executeCommand("http post --data Dracarys! --target http://localhost:9314");
		// TODO verify the output of the 'log' sink is 'Dr.rys!'
	}

	@Test
	public void testSubSubstreams() {
		executeStreamCreate("swap","transform --expression=payload.replaceAll('${from}','${to}')",false);
		executeStreamCreate("abyz","swap --from=a --to=z | swap --from=b --to=y",false);
		executeStreamCreate("foo","http --port=9314 | abyz | log",true);
		executeCommand("http post --data aabbccxxyyzz --target http://localhost:9314");
		// TODO verify log outputs zzyyccxxbbaa
	}

	@Ignore
	@Test
	public void testUsingLabels() {
		executeStreamCreate("myhttp","http --port=9314 | flibble: transform --expression=payload.toUpperCase() | log",true);
//		executeStreamCreate("wiretap","tap @myhttp.1 | transform --expression=payload.replaceAll('a','.') | log",true);
		// These variants of the above (which does work) don't appear to work although they do refer to the same source channel:
		executeStreamCreate("wiretap","tap myhttp.transform > transform --expression=payload.replaceAll('a','.') | log",true);
		executeStreamCreate("wiretap","tap myhttp.flibble > transform --expression=payload.replaceAll('a','.') | log",true);

		executeCommand("http post --data Dracarys! --target http://localhost:9314");
		// TODO verify both logs output DRACARYS!
	}

}
