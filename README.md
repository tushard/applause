# REST service to manage testers, devices, and bugs

## Design and implementation
* **Prototype using SQL:** Each of the data files testers.csv, devices.csv, testers_devices.csv, and bugs.csv correspond to a table.  So, I created four tables: Testers, Devices, TesterDevice, and Bug.  The reputation for testers in a specific countries with access to specific devices can be found using sql select query that uses joins, groupby, and orderby.  The prototype helped me obtain test data for the REST service implementation.  The sql scripts that I used can be found in ```sql``` directory.
* **REST service**: I decide to implement REST service, because it allows us to support both command line tools as well as user interface (UI).  Both command line tools and the UI would call the REST service to interact with the database.
  * **Create data model using Apache Cayenne:** [Apache Cayenne](https://cayenne.apache.org/) is an open source Java object-to-relational (ORM) mapping framework.  Cayenne is distributed with CayenneModeler - a complete GUI mapping tool that supports editing object-relational mapping projects, generation of Java source code for the persistent objects and other functions.  Using CayenneModeler, I created data model for Tester, Device, TesterDevice, and Bug tables.  Then, I used code-generation feature of Cayenne to produce Java code for my data model.
  * **Data importer module:** Wrote data importer using Cayenne query api to import data from csv files into the database.  The importer is called whenever the rest service is deployed on a web container.  The data from csv file is loaded into Apache derby in-memory database.
  * **CRUD REST service using Agrest:** [Agrest](https://agrest.io) is an open-source framework for building REST APIs for Data Stores.  It uses Apache Cayenne as the ORM provider out-of-box.  Agrest simplified implementation of REST resources that can be used to create and query various entities such as Testers and Devices.
  * **Write specialized REST resources for handling complex operations**: While Agrest simplifies writing simple CRUD services, it can be a challenge to implement complex operations using Agrest APIs.  So, I decide not to use Agrest for implementing the following operations.  INstead, I used Apache Cayenne APIs. 
    * **Create TesterDevice:** An http post call that accepts testerId and deviceId as json.  It verifies if both tester and devices are valid and exist in the database.  If yes, then it adds a new Testerdevice entry to the database.
    * **Create Bug:** An http post call that accepts bugId, testerId, and deviceId as json.  It verifies if both tester and device are valid.  It also verifies that the tester has access to the device, i.e., there exists an entry in TesterDevice entity for the tester and device received as arguments.
    * **List testers according to their reputation**: An http post call that accepts list of countries and list of devcices as json.  We can also use keywords ALL for both countries and devices.  Since, this is a search query, http get would have been the right choice to implement it.  However, I experienced some challenges while passing lists as query parameters.  I tried passing list as ```?country=US&country=JP&country=GB```.  However, tomcat seemed to pass value of the first country parameter alone to the REST resources.  Implementing reputation query as get request would have posed another challenge too.  The maximum size of htp get requets is 2048 bytes.  Although number of countries is limited to around 200, the number of devices can be very high.  So, it is likely that if we pass list of countries and list of devices as query parameters, then it would exceed the max length of the http get query.  The size of http post can be, however, as high as 4GB.   Therefore, I implemented the reputation query as http post query.

## Installation
* **Pre-requisites:** JDK 1.8, Maven, Tomcat 9
* **Build:** Clone the repo, change to ```app``` directory and run ```mvn clean install```
* **Deploy:** Copy ```app/target/applause.war``` to tomcat's ```webapps``` directory
* **Running examples:** Run the rest calls listed in the following section.  You can also execute ```RunExamples.bat``` or ```RunExamples.sh```.

## REST calls and their responses

**1) Get tester reputation with Country="ALL" and Device="iPhone 4"**
```
curl -i -X POST -H "Content-Type:application/json" -H "Accept:application/json" -d "{\"devices\":[\"iPhone 4\"], \"countries\":\"ALL\"}" http://localhost:8080/applause/rest/testerReputation
HTTP/1.1 200 
Content-Type: application/json
Content-Length: 242
Date: Mon, 04 Mar 2019 07:01:56 GMT

[{"firstName":"Taybin","lastName":"Rutkin","reputation":66},{"firstName":"Sean","lastName":"Wellington","reputation":28},{"firstName":"Miguel","lastName":"Bautista","reputation":23},{"firstName":"Mingquan","lastName":"Zheng","reputation":21}]
```

**2) Get tester reputation with Country="ALL" and Device="iPhone 4", "iPhone 5"**
```
curl -i -X POST -H "Content-Type:application/json" -H "Accept:application/json" -d "{\"devices\":[\"iPhone 4\", \"iPhone 5\"], \"countries\":\"ALL\"}" http://localhost:8080/applause/rest/testerReputation
HTTP/1.1 200 
Content-Type: application/json
Content-Length: 361
Date: Mon, 04 Mar 2019 07:02:42 GMT

[{"firstName":"Stanley","lastName":"Chen","reputation":110},{"firstName":"Taybin","lastName":"Rutkin","reputation":66},{"firstName":"Sean","lastName":"Wellington","reputation":58},{"firstName":"Miguel","lastName":"Bautista","reputation":53},{"firstName":"Leonard","lastName":"Sutton","reputation":32},{"firstName":"Mingquan","lastName":"Zheng","reputation":21}]
```

**3) Get tester reputation with Country="US" and Device="ALL"**
```
curl -i -X POST -H "Content-Type:application/json" -H "Accept:application/json" -d "{\"devices\":\"ALL\", \"countries\":[\"US\"]}" http://localhost:8080/applause/rest/testerReputation
HTTP/1.1 200 
Content-Type: application/json
Content-Length: 184
Date: Mon, 04 Mar 2019 07:03:25 GMT

[{"firstName":"Taybin","lastName":"Rutkin","reputation":125},{"firstName":"Miguel","lastName":"Bautista","reputation":114},{"firstName":"Michael","lastName":"Lubavin","reputation":99}]
```

**4) Get tester reputation with Country="US", "JP" and Device="iPhone 4", "iPhone 5", "Galaxy S4"**
```
curl -i -X POST -H "Content-Type:application/json" -H "Accept:application/json" -d "{\"devices\":[\"iPhone 4\", \"iPhone 5\", \"Galaxy S4\"], \"countries\":[\"US\", \"JP\"]}" http://localhost:8080/applause/rest/testerReputation
HTTP/1.1 200 
Content-Type: application/json
Content-Length: 360
Date: Mon, 04 Mar 2019 07:04:06 GMT

[{"firstName":"Taybin","lastName":"Rutkin","reputation":66},{"firstName":"Sean","lastName":"Wellington","reputation":58},{"firstName":"Miguel","lastName":"Bautista","reputation":53},{"firstName":"Mingquan","lastName":"Zheng","reputation":41},{"firstName":"Lucas","lastName":"Lowry","reputation":22},{"firstName":"Michael","lastName":"Lubavin","reputation":19}]
```

**5) Get tester reputation with Country="ALL" and Device="ALL"**
```
curl -i -X POST -H "Content-Type:application/json" -H "Accept:application/json" -d "{\"devices\":\"ALL\", \"countries\":\"ALL\"}" http://localhost:8080/applause/rest/testerReputation
[{"firstName":"Taybin","lastName":"Rutkin","reputation":125},{"firstName":"Lucas","lastName":"Lowry","reputation":117},{"firstName":"Sean","lastName":"Wellington","reputation":116},{"firstName":"Miguel","lastName":"Bautista","reputation":114},{"firstName":"Stanley","lastName":"Chen","reputation":110},{"firstName":"Mingquan","lastName":"Zheng","reputation":109},{"firstName":"Leonard","lastName":"Sutton","reputation":106},{"firstName":"Darshini","lastName":"Thiagarajan","reputation":104},{"firstName":"Michael","lastName":"Lubavin","reputation":99}]
```

**6) Get tester reputation with Country="JP" and Device="iPhone 4S"**
```
curl -i -X POST -H "Content-Type:application/json" -H "Accept:application/json" -d "{\"devices\":[\"iPhone 4s\"], \"countries\":[\"JP\"]}" http://localhost:8080/applause/rest/testerReputation
HTTP/1.1 200 
Content-Type: application/json
Content-Length: 2
Date: Mon, 04 Mar 2019 07:08:21 GMT

[]
```

**7) Get tester reputation with Country="US" and Device="ALL" with country not specified as array**
```
curl -i -X POST -H "Content-Type:application/json" -H "Accept:application/json" -d "{\"devices\":\"ALL\", \"countries\":\"US\"}" http://localhost:8080/applause/rest/testerReputation
HTTP/1.1 400 
Content-Type: application/json
Content-Length: 114
Date: Mon, 04 Mar 2019 06:48:42 GMT
Connection: close

{"success":false,"message":"Invalid value for countries.  Please specify a list of countries or the keyword ALL."}
```

**8) Get tester reputation with Country="ALL" and Device="iPhone 4" with device not specified as array**
```
curl -i -X POST -H "Content-Type:application/json" -H "Accept:application/json" -d "{\"devices\":\"iPhone\", \"countries\":\"ALL\"}" http://localhost:8080/applause/rest/testerReputation
HTTP/1.1 400 
Content-Type: application/json
Content-Length: 109
Date: Mon, 04 Mar 2019 06:49:18 GMT
Connection: close

{"success":false,"message":"Invalid value for devices.  Please specify a list of devices or the keyword ALL."}
```

**9) List all testers**
```
curl -i -X GET http://localhost:8080/applause/rest/tester
```

**10) List all devices**
```
curl -i -X GET http://localhost:8080/applause/rest/device
```

**11) List all testerDevices**
```
curl -i -X GET http://localhost:8080/applause/rest/testerDevice
```

**12) List all bugs**
```
curl -i -X GET http://localhost:8080/applause/rest/bug
```

**13) Create new tester**
```
curl -i -X POST -H "Content-Type:application/json" -d "{\"testerId\":10,\"firstName\":\"Tushar\", \"lastName\":\"Deshpande\", \"country\":\"IN\", \"lastLogin\":\"2019-02-04\"}" http://localhost:8080/applause/rest/tester
HTTP/1.1 201 
Content-Type: application/json
Content-Length: 16
Date: Mon, 04 Mar 2019 07:10:33 GMT

{"success":true}
```

**14) Create new testerDevice**
* **Success**
```
curl -i -X POST -H "Content-Type:application/json" -d "{\"testerId\":\"1\",\"deviceId\":\"7\"}" http://localhost:8080/applause/rest/testerDevice
HTTP/1.1 200 
Content-Type: application/json
Content-Length: 16
Date: Mon, 04 Mar 2019 06:17:15 GMT

{"success":true}
```
* **Failure: Tester or device is unknown**
```
curl -i -X POST -H "Content-Type:application/json" -d "{\"testerId\":\"1\",\"deviceId\":\"17\"}" http://localhost:8080/applause/rest/testerDevice
HTTP/1.1 400 
Content-Type: application/json
Content-Length: 58
Date: Mon, 04 Mar 2019 07:11:24 GMT
Connection: close

{"success":false,"message":"Tester or device is unknown."}
```
* **Failure: Primary key constraint violation**
```
curl -i -X POST -H "Content-Type:application/json" -d "{\"testerId\":\"1\",\"deviceId\":\"1\"}" http://localhost:8080/applause/rest/testerDevice
HTTP/1.1 500 
Content-Type: application/json
Content-Length: 246
Date: Mon, 04 Mar 2019 07:14:07 GMT
Connection: close

{"success":false,"message":"CayenneRuntimeException The statement was aborted because it would have caused a duplicate key value in a unique or primary key constraint or unique index identified by 'SQL190304020125950' defined on 'TESTERDEVICE'."}
```

**15) Submit new bug**
* **Success**
```
curl -i -X POST -H "Content-Type:application/json" -d "{\"bugId\":\"2003\",\"testerId\":\"2\",\"deviceId\":\"7\"}" http://localhost:8080/applause/rest/bug
HTTP/1.1 200 
Content-Type: application/json
Content-Length: 16
Date: Mon, 04 Mar 2019 06:58:13 GMT

{"success":true}
```
* **Failure: A tester must register a device before submitting bug for it**
```
curl -i -X POST -H "Content-Type:application/json" -d "{\"bugId\":\"2005\",\"testerId\":\"2\",\"deviceId\":\"2\"}" http://localhost:8080/applause/rest/bug
HTTP/1.1 400 
Content-Type: application/json
Content-Length: 91
Date: Mon, 04 Mar 2019 06:57:23 GMT
Connection: close

{"success":false,"message":"A tester must register a device before submitting bug for it."}
```
* **Failure: Tester or device is unknown**
```
curl -i -X POST -H "Content-Type:application/json" -d "{\"bugId\":\"2005\",\"testerId\":\"2\",\"deviceId\":\"17\"}" http://localhost:8080/applause/rest/bug
HTTP/1.1 400 
Content-Type: application/json
Content-Length: 57
Date: Mon, 04 Mar 2019 06:59:15 GMT
Connection: close

{"success":false,"message":"Tester or device is unknown"}
```
## ToDo
* Improve validation of imput parameters of post calls to check for null or empty json data.
* Write unit tests.
* Build REST client in various languages, such as Java, python, perl, to simplify calling the REST services.
* Build UI.