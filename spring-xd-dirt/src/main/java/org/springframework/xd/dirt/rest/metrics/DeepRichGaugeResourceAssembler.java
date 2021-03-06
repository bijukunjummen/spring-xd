/*
 * Copyright 2013 the original author or authors.
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

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.xd.analytics.metrics.core.Gauge;
import org.springframework.xd.analytics.metrics.core.RichGauge;
import org.springframework.xd.rest.client.domain.metrics.GaugeResource;
import org.springframework.xd.rest.client.domain.metrics.RichGaugeResource;

/**
 * Knows how to assemble {@link RichGaugeResource}s out of {@link RichGauge}s.
 *
 * @author Luke Taylor
 */
class DeepRichGaugeResourceAssembler extends
		ResourceAssemblerSupport<RichGauge, RichGaugeResource> {

	public DeepRichGaugeResourceAssembler() {
		super(RichGaugesController.class, RichGaugeResource.class);
	}

	@Override
	public RichGaugeResource toResource(RichGauge entity) {
		return createResourceWithId(entity.getName(), entity);
	}

	@Override
	protected RichGaugeResource instantiateResource(RichGauge entity) {
		return new RichGaugeResource(entity.getName(), entity.getValue(), entity.getAverage(), entity.getMax(), entity.getMin());
	}

}
