package com.applause.cloudsource.app.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.query.ObjectSelect;

import com.applause.cloudsource.app.persistent.Bug;
import com.applause.cloudsource.app.persistent.Device;
import com.applause.cloudsource.app.persistent.Tester;
import com.applause.cloudsource.app.persistent.TesterDevice;

public class DataImporter {

	public static void load(ServerRuntime cayenneRuntime) throws IOException, ParseException {
		load(cayenneRuntime.newContext());
	}
	
	private static void load(ObjectContext context) throws IOException, ParseException {
		loadTesters(context, "data/testers.csv");
		loadDevices(context, "data/devices.csv");
		loadTesterDevices(context, "data/tester_device.csv");
		loadBugs(context, "data/bugs.csv");
	}

	private static void loadTesters(ObjectContext context, String fileName) throws IOException, ParseException {
		BufferedReader reader = new BufferedReader(new FileReader(DataImporter.class.getClassLoader().getResource(fileName).getFile()));
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String line = reader.readLine();
		while ((line = reader.readLine()) != null) {
			String[] fields = line.split(",");
			String testerId = fields[0].replaceAll("\"", "");
			String fName = fields[1].replaceAll("\"", "");
			String lName = fields[2].replaceAll("\"", "");
			String country = fields[3].replaceAll("\"", "");
			Date dateStr = dateFormatter.parse(fields[4].replaceAll("\"", ""));
			LocalDate lastLoginDate = dateStr.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			Tester tester = context.newObject(Tester.class);
			tester.setTesterId(testerId);
			tester.setFirstName(fName);
			tester.setLastName(lName);
			tester.setCountry(country);
			tester.setLastLogin(lastLoginDate);

			context.commitChanges();

		}
		reader.close();
	}

	private static void loadDevices(ObjectContext context, String fileName) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(DataImporter.class.getClassLoader().getResource(fileName).getFile()));
		String line = reader.readLine();
		while ((line = reader.readLine()) != null) {
			String[] fields = line.split(",");
			String deviceId = fields[0].replaceAll("\"", "");
			String description = fields[1].replaceAll("\"", "");

			Device device = context.newObject(Device.class);
			device.setDeviceId(deviceId);
			device.setDescription(description);

			context.commitChanges();

		}
		reader.close();
	}

	private static void loadTesterDevices(ObjectContext context, String fileName) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(DataImporter.class.getClassLoader().getResource(fileName).getFile()));
		String line = reader.readLine();
		while ((line = reader.readLine()) != null) {
			String[] fields = line.split(",");
			String testerId = fields[0].replaceAll("\"", "");
			String deviceId = fields[1].replaceAll("\"", "");

			Tester tester = ObjectSelect.query(Tester.class).where(Tester.TESTER_ID.eq(testerId)).select(context).get(0);
			Device device = ObjectSelect.query(Device.class).where(Device.DEVICE_ID.eq(deviceId)).select(context).get(0);

			TesterDevice testerDevice = context.newObject(TesterDevice.class);
			testerDevice.setTester(tester);
			testerDevice.setDevice(device);

			context.commitChanges();

		}
		reader.close();
	}

	private static void loadBugs(ObjectContext context, String fileName) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(DataImporter.class.getClassLoader().getResource(fileName).getFile()));
		String line = reader.readLine();
		while ((line = reader.readLine()) != null) {
			String[] fields = line.split(",");
			String bugId = fields[0].replaceAll("\"", "");
			String deviceId = fields[1].replaceAll("\"", "");
			String testerId = fields[2].replaceAll("\"", "");

			Tester tester = ObjectSelect.query(Tester.class).where(Tester.TESTER_ID.eq(testerId)).select(context).get(0);
			Device device = ObjectSelect.query(Device.class).where(Device.DEVICE_ID.eq(deviceId)).select(context).get(0);

			boolean testerHasDevice = false;
			for (TesterDevice testerdevice : tester.getDevices()) {
				if (testerdevice.getDevice().equals(device)) {
					testerHasDevice = true;
					Bug bug = context.newObject(Bug.class);
					bug.setBugId(bugId);
					bug.setTester(tester);
					bug.setDevice(device);
					context.commitChanges();
					break;
				}
			}
			if (!testerHasDevice) {
				reader.close();
				throw new IllegalArgumentException(
						"Testers can log bugs for only those devices that they have registerd with Applause");
			}

		}
		reader.close();
	}

}
