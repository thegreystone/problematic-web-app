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
package se.hirt.examples.problematicwebapp.rest;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import se.hirt.examples.problematicwebapp.data.Customer;
import se.hirt.examples.problematicwebapp.data.DataAccess;
import se.hirt.examples.problematicwebapp.data.ValidationException;

/**
 * Rest API for customers.
 * 
 * @author Marcus Hirt
 */
@Path("/customers/")
public class CustomersResource {
	@Context
	UriInfo uriInfo;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JsonArray list() {
		JsonArrayBuilder uriArrayBuilder = Json.createArrayBuilder();
		for (Customer customer : DataAccess.getAllCustomers()) {
			UriBuilder ub = uriInfo.getAbsolutePathBuilder();
			URI userUri = ub.path(String.valueOf(customer.getCustomerId())).build();
			uriArrayBuilder.add(userUri.toASCIIString());
		}
		return uriArrayBuilder.build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putUser(JsonObject jsonEntity) {
		try {
			return Response.accepted(decodeCustomer(jsonEntity)).build();
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_ACCEPTABLE.getStatusCode(), e.getMessage()).build();
		}
	}

	private static Customer decodeCustomer(JsonObject jsonEntity) throws ValidationException {
		String fullName = jsonEntity.getString(CustomerKeys.FULL_NAME);
		String phoneNumber = jsonEntity.getString(CustomerKeys.PHONE_NUMBER);
		Customer.validate(fullName, phoneNumber);
		return DataAccess.createCustomer(fullName, phoneNumber);
	}

	@PUT
	@Path("batchadd/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putUsers(JsonObject jsonEntity) {
		JsonArray jsonArray = jsonEntity.getJsonArray("customers");
		List<Customer> customers = jsonArray.stream().map((jsonValue) -> {
			try {
				return decodeCustomer(jsonValue.asJsonObject());
			} catch (ValidationException e) {
				return null;
			}
		}).collect(Collectors.toList());
		return Response.accepted(customers).build();
	}

	@Path("{customerId}/")
	public CustomerResource getCustomer(@PathParam("customerId") String customerId) {
		return new CustomerResource(uriInfo, customerId);
	}
}
