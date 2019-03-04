##Various REST calls and their responses

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
