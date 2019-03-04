create table Testers (
	testerId INT,
    firstName VARCHAR(255) NOT NULL,
    lastName VARCHAR(255) NOT NULL,
    country VARCHAR(2) NOT NULL,
    lastLogin DATE,
    PRIMARY KEY (testerId)
);

create table Devices (
	deviceId INT,
    description VARCHAR(255) NOT NULL,
    PRIMARY KEY (deviceId)
);

create table TesterDevice (
	testerId INT NOT NULL,
	deviceId INT NOT NULL,
    PRIMARY KEY (testerId, deviceId),
    FOREIGN KEY fk_testerDevice_testerId(testerId) REFERENCES testers(testerId),
    FOREIGN KEY fk_testerDevice_deviceId(deviceId) REFERENCES devices(deviceId)
);

create table Bug (
	bugId INT,
	deviceId INT NOT NULL,
    testerId INT NOT NULL,
    PRIMARY KEY (bugId),
    FOREIGN KEY fk_bug_testerId(testerId) REFERENCES testers(testerId),
    FOREIGN KEY fk_bug_deviceId(deviceId) REFERENCES devices(deviceId)
);

commit;

