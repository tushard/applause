package com.applause.cloudsource.app;

import java.io.IOException;
import java.text.ParseException;

import javax.ws.rs.ApplicationPath;

import org.apache.cayenne.configuration.server.ServerRuntime;
import org.glassfish.jersey.server.ResourceConfig;

import com.applause.cloudsource.app.tools.DataImporter;

import io.agrest.runtime.AgBuilder;
import io.agrest.runtime.AgRuntime;

@ApplicationPath("apprest")
public class AppRest extends ResourceConfig {
	public AppRest() {
		ServerRuntime cayenneRuntime = CaynneRuntimeSingleton.getInstance();
		try {
			DataImporter.load(cayenneRuntime);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		AgRuntime lrRuntime = AgBuilder.build(cayenneRuntime);
		super.register(lrRuntime);
		packages("com.applause.cloudsource.app.rest");
	}
}
