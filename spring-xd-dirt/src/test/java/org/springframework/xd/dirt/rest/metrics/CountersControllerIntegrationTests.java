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

package org.springframework.xd.dirt.rest.metrics;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.xd.analytics.metrics.core.Counter;
import org.springframework.xd.dirt.rest.AbstractControllerIntegrationTest;
import org.springframework.xd.dirt.rest.MockedDependencies;
import org.springframework.xd.dirt.rest.RestConfiguration;

import scala.actors.threadpool.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests proper behavior of {@link CountersController}.
 * 
 * @author Eric Bottard
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { RestConfiguration.class, MockedDependencies.class })
public class CountersControllerIntegrationTests extends AbstractControllerIntegrationTest {

	@Test
	public void testInexistantCounterRetrieval() throws Exception {
		mockMvc.perform(get("/metrics/counters/notthere")).andExpect(status().isNotFound());
	}

	@Test
	public void testExistingCounterRetrieval() throws Exception {
		when(counterRepository.findOne("iamthere")).thenReturn(new Counter("iamthere", 12L));

		mockMvc.perform(get("/metrics/counters/iamthere"))//
		.andExpect(status().isOk())//
		.andExpect(jsonPath("$.name").value("iamthere"))//
		.andExpect(jsonPath("$.value").value(12));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testCounterListing() throws Exception {
		Counter[] counters = new Counter[10];
		for (int i = 0; i < counters.length; i++) {
			counters[i] = new Counter("c" + i, i);
		}
		when(counterRepository.findAll()).thenReturn(Arrays.asList(counters));

		mockMvc.perform(get("/metrics/counters")).andExpect(status().isOk())//
		.andExpect(jsonPath("$.content", Matchers.hasSize(10)));
	}
}
