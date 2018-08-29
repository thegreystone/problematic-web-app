package se.hirt.examples.problematicwebapp.rest;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import se.hirt.examples.problematicwebapp.data.Customer;
import se.hirt.examples.problematicwebapp.data.DataAccess;

public class CustomerResource {

	private final UriInfo uriInfo;
	private final String customerId;
	private final Customer customer;

	public CustomerResource(UriInfo uriInfo, String customerId) {
		this.uriInfo = uriInfo;
		this.customerId = customerId;
		this.customer = DataAccess.getCustomerById(Long.valueOf(customerId));
	}

	public UriInfo getUriInfo() {
		return uriInfo;
	}

	public String getCustomerId() {
		return customerId;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject getCustomer() {
		if (null == customer) {
			throw new NotFoundException("customerId " + customerId + " does not exist!");
		}
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add("customerId", customer.getCustomerId());
		builder.add("fullName", customer.getFullName());
		builder.add("phoneNumber", customer.getPhoneNumber());
		return builder.build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putUser(JsonObject jsonEntity) {
		String jsonCustomerId = jsonEntity.getString("customerId");

		if ((jsonCustomerId != null) && !jsonCustomerId.equals(customerId)) {
			return Response.status(409).entity("customerIds differ!\n").build();
		}

		// If we have no customer, this is an insert, otherwise an update
		final boolean newRecord = (null == customer);

		String fullName = jsonEntity.getString(CustomerKeys.FULL_NAME);
		String phoneNumber = jsonEntity.getString(CustomerKeys.PHONE_NUMBER);

		if (newRecord) {
			DataAccess.createCustomer(fullName, phoneNumber);
			return Response.created(uriInfo.getAbsolutePath()).build();
		} else {
			DataAccess.updateCustomer(Long.valueOf(jsonCustomerId), fullName, phoneNumber);
			return Response.noContent().build();
		}
	}
	
    @DELETE
    public void deleteUser() {
        if (customer == null) {
            throw new NotFoundException("customerId " + customerId + "does not exist!");
        }
        DataAccess.removeCustomer(customer);
    }
    
}
