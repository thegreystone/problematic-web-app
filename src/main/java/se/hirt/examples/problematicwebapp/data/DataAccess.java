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
package se.hirt.examples.problematicwebapp.data;

import java.util.Collection;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.flakeidgen.FlakeIdGenerator;
import com.hazelcast.map.listener.MapListener;

/**
 * Memory grid data access.
 * 
 * @author Marcus Hirt
 */
public class DataAccess {
	private final static IMap<Long, Customer> CUSTOMERS;
	private final static IMap<String, Customer> CUSTOMERS_INDEX_BY_NAME;
	private final static IMap<String, Customer> CUSTOMERS_INDEX_BY_PHONE;

	private final static FlakeIdGenerator CUSTOMER_ID_GENERATOR;

	static {
		Config cfg = new Config();
		cfg.setClassLoader(Customer.class.getClassLoader());
		HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
		CUSTOMER_ID_GENERATOR = instance.getFlakeIdGenerator("customerIdGenerator");
		CUSTOMERS = instance.getMap("customers");
		CUSTOMERS_INDEX_BY_NAME = instance.getMap("customersIndexByName");
		CUSTOMERS_INDEX_BY_PHONE = instance.getMap("customersIndexByPhone");
	}

	public static Customer createCustomer(String fullName, String phoneNumber) {
		Customer newCustomer = new Customer(CUSTOMER_ID_GENERATOR.newId(), fullName, phoneNumber);
		CUSTOMERS.put(newCustomer.getId(), newCustomer);
		CUSTOMERS_INDEX_BY_NAME.put(newCustomer.getFullName(), newCustomer);
		CUSTOMERS_INDEX_BY_PHONE.put(newCustomer.getPhoneNumber(), newCustomer);
		return newCustomer;
	}

	public static Collection<Customer> getAllCustomers() {
		return CUSTOMERS.values();
	}

	public static Customer getCustomerByName(String fullName) {
		return CUSTOMERS_INDEX_BY_NAME.get(fullName);
	}

	public static Customer getCustomerByPhone(String phone) {
		return CUSTOMERS_INDEX_BY_PHONE.get(phone);
	}

	public static void removeCustomer(Customer customer) {
		CUSTOMERS.delete(customer.getId());
		CUSTOMERS_INDEX_BY_NAME.delete(customer.getFullName());
		CUSTOMERS_INDEX_BY_PHONE.delete(customer.getId()); // <--Hint!
	}

	public static int getNumberOfCustomers() {
		return CUSTOMERS.size();
	}

	public static Customer getCustomerById(Long id) {
		return CUSTOMERS.get(id);
	}

	public static void updateCustomer(Long id, String fullName, String phoneNumber) {
		Customer updated = new Customer(id, fullName, phoneNumber);
		CUSTOMERS.replace(id, updated);
	}

	/**
	 * Adds a listener for getting notified on changes to the customers.
	 * 
	 * @param listener
	 *            the listener to add.
	 * @return the ID used to identify the listener on subsequent removal.
	 */
	public static String addCustomersListener(MapListener listener) {
		return CUSTOMERS.addEntryListener(listener, true);
	}

	/**
	 * Removes a previously registered listener. 
	 * 
	 * @param id the id of the listener to remove.
	 */
	public static void removeCustomersListener(String id) {
		CUSTOMERS.removeEntryListener(id);
	}

}
