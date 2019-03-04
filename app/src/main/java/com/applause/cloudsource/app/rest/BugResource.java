package com.applause.cloudsource.app.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;

import com.applause.cloudsource.app.CaynneRuntimeSingleton;
import com.applause.cloudsource.app.persistent.Bug;
import com.applause.cloudsource.app.persistent.Device;
import com.applause.cloudsource.app.persistent.Tester;
import com.applause.cloudsource.app.persistent.TesterDevice;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.agrest.Ag;
import io.agrest.DataResponse;

@Path("bug")
@Produces(MediaType.APPLICATION_JSON)
public class BugResource {

	@Context
	private Configuration config;

	private ObjectContext context = CaynneRuntimeSingleton.getInstance().newContext();

	@GET
	public DataResponse<Bug> getAll(@Context UriInfo uriInfo) {
		return Ag.select(Bug.class, config).uri(uriInfo).get();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(String jsonAsString) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(jsonAsString);
		String bugId = root.get("bugId").asText();
		String testerId = root.get("testerId").asText();
		String deviceId = root.get("deviceId").asText();

		List<Tester> testers = ObjectSelect.query(Tester.class).where(Tester.TESTER_ID.eq(testerId)).select(context);
		List<Device> devices = ObjectSelect.query(Device.class).where(Device.DEVICE_ID.eq(deviceId)).select(context);

		int responseCode = 200;
		ObjectNode response = null;

		if (testers == null || testers.isEmpty() || devices == null || devices.isEmpty()) {
			responseCode = 400;
			response = mapper.createObjectNode();
			response.put("success", false);
			response.put("message", "Tester or device is unknown");
		} else {
			Tester tester = testers.get(0);
			Device device = devices.get(0);

			boolean testerHasDevice = false;
			for (TesterDevice testerdevice : tester.getDevices()) {
				if (testerdevice.getDevice().equals(device)) {
					testerHasDevice = true;
					Bug bug = context.newObject(Bug.class);
					bug.setBugId(bugId);
					bug.setTester(tester);
					bug.setDevice(device);
					context.commitChanges();
					response = mapper.createObjectNode();
					response.put("success", true);
					break;
				}
			}
			if (!testerHasDevice) {
				responseCode = 400;
				response = mapper.createObjectNode();
				response.put("success", false);
				response.put("message",
						"A tester must register a device before submitting bug for it.");
			}
		}

		return Response.status(responseCode).entity(mapper.writeValueAsString(response)).build();
	}

}
