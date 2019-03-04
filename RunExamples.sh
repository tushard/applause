#!/bin/bash

curl -i -X POST -H "Content-Type:application/json" -H "Accept:application/json" -d "{\"devices\":[\"iPhone 4\"], \"countries\":\"ALL\"}" http://localhost:8080/applause/rest/testerReputation
curl -i -X POST -H "Content-Type:application/json" -H "Accept:application/json" -d "{\"devices\":[\"iPhone 4\", \"iPhone 5\"], \"countries\":\"ALL\"}" http://localhost:8080/applause/rest/testerReputation
curl -i -X POST -H "Content-Type:application/json" -H "Accept:application/json" -d "{\"devices\":\"ALL\", \"countries\":[\"US\"]}" http://localhost:8080/applause/rest/testerReputation
curl -i -X POST -H "Content-Type:application/json" -H "Accept:application/json" -d "{\"devices\":[\"iPhone 4\", \"iPhone 5\", \"Galaxy S4\"], \"countries\":[\"US\", \"JP\"]}" http://localhost:8080/applause/rest/testerReputation
curl -i -X POST -H "Content-Type:application/json" -H "Accept:application/json" -d "{\"devices\":\"ALL\", \"countries\":\"ALL\"}" http://localhost:8080/applause/rest/testerReputation
curl -i -X POST -H "Content-Type:application/json" -H "Accept:application/json" -d "{\"devices\":[\"iPhone 4s\"], \"countries\":[\"JP\"]}" http://localhost:8080/applause/rest/testerReputation
curl -i -X POST -H "Content-Type:application/json" -H "Accept:application/json" -d "{\"devices\":\"ALL\", \"countries\":\"US\"}" http://localhost:8080/applause/rest/testerReputation
curl -i -X POST -H "Content-Type:application/json" -H "Accept:application/json" -d "{\"devices\":\"iPhone\", \"countries\":\"ALL\"}" http://localhost:8080/applause/rest/testerReputation
curl -i -X GET http://localhost:8080/applause/rest/tester
curl -i -X GET http://localhost:8080/applause/rest/device
curl -i -X GET http://localhost:8080/applause/rest/testerDevice
curl -i -X GET http://localhost:8080/applause/rest/bug
curl -i -X POST -H "Content-Type:application/json" -d "{\"testerId\":10,\"firstName\":\"Tushar\", \"lastName\":\"Deshpande\", \"country\":\"IN\", \"lastLogin\":\"2019-02-04\"}" http://localhost:8080/applause/rest/tester
curl -i -X POST -H "Content-Type:application/json" -d "{\"testerId\":\"1\",\"deviceId\":\"7\"}" http://localhost:8080/applause/rest/testerDevice
curl -i -X POST -H "Content-Type:application/json" -d "{\"testerId\":\"1\",\"deviceId\":\"17\"}" http://localhost:8080/applause/rest/testerDevice
curl -i -X POST -H "Content-Type:application/json" -d "{\"testerId\":\"1\",\"deviceId\":\"1\"}" http://localhost:8080/applause/rest/testerDevice
curl -i -X POST -H "Content-Type:application/json" -d "{\"bugId\":\"2003\",\"testerId\":\"2\",\"deviceId\":\"7\"}" http://localhost:8080/applause/rest/bug
curl -i -X POST -H "Content-Type:application/json" -d "{\"bugId\":\"2005\",\"testerId\":\"2\",\"deviceId\":\"2\"}" http://localhost:8080/applause/rest/bug
curl -i -X POST -H "Content-Type:application/json" -d "{\"bugId\":\"2005\",\"testerId\":\"2\",\"deviceId\":\"17\"}" http://localhost:8080/applause/rest/bug