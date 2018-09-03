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

import java.util.HashMap;
import java.util.Map;

import javax.websocket.Session;

import se.hirt.examples.problematicwebapp.data.DataAccess;

/**
 * Service to listen for additions and removals of customers.
 * 
 * @author Marcus Hirt
 */
public class CustomersService {
	private final static CustomersService INSTANCE = new CustomersService();
	private final Map<Session, SessionMapListener> listeners = new HashMap<>();
		
	private CustomersService() {
	}
	
	public static CustomersService getInstance() {
		return INSTANCE;
	}
	
	public void addSession(Session session) {
		SessionMapListener mapListener = new SessionMapListener(session);
		String id = DataAccess.addCustomersListener(mapListener);
		mapListener.setId(id);
		listeners.put(session, mapListener);
	}
	
	public void removeSession(Session session) {
		String id = listeners.get(session).getId();
		listeners.remove(session);
		DataAccess.removeCustomersListener(id);
	}	
}
