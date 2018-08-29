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
		return Response.accepted(decodeCustomer(jsonEntity)).build();
	}

	private static Customer decodeCustomer(JsonObject jsonEntity) {
		return DataAccess.createCustomer(jsonEntity.getString(CustomerKeys.FULL_NAME),
				jsonEntity.getString(CustomerKeys.PHONE_NUMBER));
	}

	@PUT
	@Path("batchadd/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putUsers(JsonObject jsonEntity) {
		JsonArray jsonArray = jsonEntity.getJsonArray("customers");
		List<Customer> customers = jsonArray.stream().map((jsonValue) -> decodeCustomer(jsonValue.asJsonObject()))
				.collect(Collectors.toList());
		return Response.accepted(customers).build();
	}

	@Path("{customerId}/")
	public CustomerResource getCustomer(@PathParam("customerId") String customerId) {
		return new CustomerResource(uriInfo, customerId);
	}
}
