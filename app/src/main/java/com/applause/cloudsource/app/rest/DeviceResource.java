package com.applause.cloudsource.app.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.applause.cloudsource.app.persistent.Device;

import io.agrest.Ag;
import io.agrest.DataResponse;
import io.agrest.SimpleResponse;

@Path("device")
@Produces(MediaType.APPLICATION_JSON)
public class DeviceResource {
	@Context
    private Configuration config;

    @GET
    public DataResponse<Device> getAll(@Context UriInfo uriInfo) {
        return Ag.select(Device.class, config).uri(uriInfo).get();
    }
    
    @POST
    public SimpleResponse create(String data) {
        return Ag.create(Device.class, config).sync(data);
    }
}
