/*
 * Copyright (C) 2018 Marcus Hirt
 *                    www.hirt.se
 *
 * This software is free:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESSED OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright (C) Marcus Hirt, 2018
 */
package se.hirt.examples.problematicwebapp.websocket;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.websocket.Session;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryRemovedListener;
import com.hazelcast.map.listener.MapListener;

import se.hirt.examples.problematicwebapp.data.Customer;
import se.hirt.examples.problematicwebapp.rest.CustomerKeys;

/**
 * In real world, I assume it would be prudent to batch up the changes and update every N seconds or
 * so.
 * 
 * @author Marcus Hirt
 */
public class SessionMapListener
		implements MapListener, EntryAddedListener<Long, Customer>, EntryRemovedListener<Long, Customer> {
	private final Session session;
	private String id;

	public SessionMapListener(Session session) {
		this.session = session;
	}

	@Override
	public void entryAdded(EntryEvent<Long, Customer> event) {
		System.out.println("Added: " + event);
		JsonObjectBuilder builder = Customer.toJSon(event.getValue());
		builder.add("action", "add");
		send(builder.build().toString());
	}

	@Override
	public void entryRemoved(EntryEvent<Long, Customer> event) {
		System.out.println("Removed: " + event);
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add(CustomerKeys.ID, String.valueOf(event.getKey()));
		builder.add("action", "remove");
		send(builder.build().toString());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private void send(String string) {
		try {
			session.getBasicRemote().sendText(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
