package com.applause.cloudsource.app.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.Property;
import org.apache.cayenne.query.ObjectSelect;

import com.applause.cloudsource.app.CaynneRuntimeSingleton;
import com.applause.cloudsource.app.persistent.Bug;
import com.applause.cloudsource.app.persistent.Device;
import com.applause.cloudsource.app.persistent.Tester;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Path("/testerReputation")
public class TesterReputationResource {

	private ObjectContext context = CaynneRuntimeSingleton.getInstance().newContext();

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getTesterReputation(String jsonAsString) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(jsonAsString);
		JsonNode countriesJson = root.get("countries");
		JsonNode devicesJson = root.get("devices");
		List<String> countries = new ArrayList<>();
		if (countriesJson.isArray()) {
			for (final JsonNode country : countriesJson) {
				countries.add(country.asText());
			}
		} else if (!countriesJson.asText().equalsIgnoreCase("ALL")) {
			int responseCode = 400;
			ObjectNode response = mapper.createObjectNode();
			response.put("success", false);
			response.put("message",
					"Invalid value for countries.  Please specify a list of countries or the keyword ALL.");
			return Response.status(responseCode).entity(mapper.writeValueAsString(response)).build();
		}
		List<String> devices = new ArrayList<>();
		if (devicesJson.isArray()) {
			for (final JsonNode device : devicesJson) {
				devices.add(device.asText());
			}
		} else if (!devicesJson.asText().equalsIgnoreCase("ALL")) {
			int responseCode = 400;
			ObjectNode response = mapper.createObjectNode();
			response.put("success", false);
			response.put("message", "Invalid value for devices.  Please specify a list of devices or the keyword ALL.");
			return Response.status(responseCode).entity(mapper.writeValueAsString(response)).build();
		}

		List<Object[]> testersWithReputation = null;
		Property<Long> bugCount = Bug.TESTER.count();
		if (countries == null || countries.size() == 0) {
			if (devices == null || devices.size() == 0) {
				testersWithReputation = ObjectSelect.query(Bug.class).columns(Bug.TESTER, bugCount)
						.orderBy(bugCount.desc()).select(context);
			} else {
				testersWithReputation = ObjectSelect.query(Bug.class)
						.where(Bug.DEVICE.dot(Device.DESCRIPTION).in(devices)).columns(Bug.TESTER, bugCount)
						.orderBy(bugCount.desc()).select(context);
			}
		} else {
			if (devices == null || devices.size() == 0) {
				testersWithReputation = ObjectSelect.query(Bug.class)
						.where(Bug.TESTER.dot(Tester.COUNTRY).in(countries)).columns(Bug.TESTER, bugCount)
						.orderBy(bugCount.desc()).select(context);
			} else {
				testersWithReputation = ObjectSelect.query(Bug.class)
						.where(Bug.TESTER.dot(Tester.COUNTRY).in(countries))
						.and(Bug.DEVICE.dot(Device.DESCRIPTION).in(devices)).columns(Bug.TESTER, bugCount)
						.orderBy(bugCount.desc()).select(context);
			}
		}

		List<TesterReputation> response = new ArrayList<>();

		for (Object[] testerWithReputation : testersWithReputation) {
			Tester tester = (Tester) testerWithReputation[0];
			Long reputation = (Long) testerWithReputation[1];
			TesterReputation testerReputation = new TesterReputation();
			testerReputation.setFirstName(tester.getFirstName());
			testerReputation.setLastName(tester.getLastName());
			testerReputation.setReputation(reputation.intValue());
			response.add(testerReputation);
		}

		TesterReputation[] arr = new TesterReputation[0];
		arr = response.toArray(arr);
		return Response.status(200).entity(arr).build();
	}

}