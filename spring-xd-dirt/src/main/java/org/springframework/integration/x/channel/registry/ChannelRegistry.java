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

package org.springframework.integration.x.channel.registry;

import java.util.Collection;

import org.springframework.http.MediaType;
import org.springframework.integration.MessageChannel;

/**
 * A strategy interface used to bind a {@link MessageChannel} to a logical name. The name is intended to identify a
 * logical consumer or producer of messages. This may be a queue, a channel adapter, another message channel, a Spring
 * bean, etc.
 * 
 * @author Mark Fisher
 * @author David Turanski
 * @author Gary Russell
 * @since 1.0
 */
public interface ChannelRegistry {

	/**
	 * Register a message consumer.
	 * 
	 * @param name the logical identity of the message source
	 * @param moduleInputChannel the channel bound as a consumer
	 * @param acceptedMediaTypes the media types supported by the channel
	 * @param aliasHint whether the provided name represents an alias and thus should support late binding
	 */
	void inbound(String name, MessageChannel moduleInputChannel, Collection<MediaType> acceptedMediaTypes,
			boolean aliasHint);

	/**
	 * Register a message producer.
	 * 
	 * @param name the logical identity of the message target
	 * @param moduleOutputChannel the channel bound as a producer
	 * @param aliasHint whether the provided name represents an alias and thus should support late binding
	 */
	void outbound(String name, MessageChannel moduleOutputChannel, boolean aliasHint);

	/**
	 * Create a tap on an already registered inbound channel.
	 * 
	 * @param tapModule the name of the tap module
	 * @param name the registered name
	 * @param channel the channel that will receive messages from the tap
	 */
	void tap(String tapModule, String name, MessageChannel channel);

	/**
	 * Remove all subscriptions to inter-module channels for this module and stop any active components that use those
	 * channels.
	 * @param name the module name
	 */
	void cleanAll(String name);
}
