package com.applause.cloudsource.app;

import org.apache.cayenne.configuration.server.ServerRuntime;

public class CaynneRuntimeSingleton {

	private static ServerRuntime instance = null;

	public static ServerRuntime getInstance() {
		if (instance == null) {
			synchronized (CaynneRuntimeSingleton.class) {
				if (instance == null) {
					instance = ServerRuntime.builder().addConfig("cayenne-applause.xml").build();
				}
			}
		}
		return instance;
	}

}
