# Consumer Driven Contract Testing using Pact

This project is created in order to demonstrate how consumer driven contract testing can be performed using 
[Pact](https://docs.pact.io/) for the following communication methods.

* REST
* Graph QL
* gRPC
* Async Event Publish
* Async Request/Command Send

It consists of four different applications written with Kotlin and Spring Boot, proto files used during gRPC 
communication and for sharing payload between asynchronously communicating parties.

##Pact Broker
[Pact Broker](https://github.com/pact-foundation/pact_broker) is used to share contract interations and verifications 
between consumer and provider services. You need to make Pact Broker running on **80** port in your local environment in 
order to be able to execute existing contract tests. You can follow the steps provided within this 
[page](https://github.com/DiUS/pact_broker-docker/blob/master/POSTGRESQL.md) to install and make it running.

##Consumer-Provider Summary Table

Following is the summary table which shows services and their consumer/provider roles while performing contract testing
of different communication methods.

|Consumer|Provider|Comm. Method|
|--------|--------|------------|
|a-service|b-service|REST|
|b-service|c-service|gRPC|
|b-service|d-service|GraphQL|
|c-service|d-service|Async Event|
|a-service|d-service|Async Request/Command|

##General Information about Services & Their Contract Tests

###a-service

* Demonstrates consumer driven contract testing of 
  [REST](https://github.com/ksevindik/pact-io-contract-tests/blob/master/a-service/src/test/kotlin/com/example/a/RestConsumerTests.kt) 
  and 
  [Async Request/Command](https://github.com/ksevindik/pact-io-contract-tests/blob/master/a-service/src/test/kotlin/com/example/a/AsyncRequestConsumerTests.kt) 
  communication mechanisms with the consumer role.
* Exposes REST endpoints to perform experiments via [Swagger UI](http://localhost:8083).

###b-service

* Demonstrates consumer driven contract testing of 
  [gRPC](https://github.com/ksevindik/pact-io-contract-tests/blob/master/b-service/src/test/kotlin/com/example/b/GrpcConsumerTests.kt) 
  and 
  [GraphQL](https://github.com/ksevindik/pact-io-contract-tests/blob/master/b-service/src/test/kotlin/com/example/b/GraphQLConsumerTests.kt) 
  communication mechanisms with the consumer role.
* Demonstrates consumer driven contract testing of 
  [REST](https://github.com/ksevindik/pact-io-contract-tests/blob/master/b-service/src/test/kotlin/com/example/b/RestProviderTests.kt) 
  communication mechanism with the provider role.
* Exposes REST endpoints to perform experiments via [Swagger UI](http://localhost:8084).

###c-service

* Demonstrates consumer driven contract testing of 
  [async event publish](https://github.com/ksevindik/pact-io-contract-tests/blob/master/c-service/src/test/kotlin/com/example/c/AsyncEventConsumerTests.kt) 
  with the consumer role.
* Demonstrates consumer driven contract testing of 
  [gRPC](https://github.com/ksevindik/pact-io-contract-tests/blob/master/c-service/src/test/kotlin/com/example/c/GrpcProviderTests.kt) 
  with the provider role.
* Exposes REST endpoints to perform experiments via [Swagger UI](http://localhost:8085).

###d-service

* Demonstrates consumer driven contract testing of 
  [GraphQL](https://github.com/ksevindik/pact-io-contract-tests/blob/master/d-service/src/test/kotlin/com/example/d/GraphQLProviderTests.kt) , 
  [async event publish](https://github.com/ksevindik/pact-io-contract-tests/blob/master/d-service/src/test/kotlin/com/example/d/AsyncEventProviderTests.kt) 
  and 
  [async request/command](https://github.com/ksevindik/pact-io-contract-tests/blob/master/d-service/src/test/kotlin/com/example/d/AsyncRequestProviderTests.kt) 
  with the 
  provider role.
* Exposes REST endpoints to perform experiments via [Swagger UI](http://localhost:8086).
* GraphiQL Explorer is also accessible via http://localhost:8086/graphiql endpoint.

**Note:** Currently, async communication endpoints just print events/messages to the console. In the future, Kafka or
any other message broker might be added to demonstrate the async communication while services running.

**Note:** There is also no persistent storage7mechanism used to retrieve or save data processed within services, sample 
data is simple returned from memory. In the future, H2 in memory database and Spring Data Repository might be added 
instead of returning hard coded sample data from the memory.

However, neither of those parts are prerequisite to assess how consumer driven contract
testing using Pact can be performed for all those different communication methods.

##Port Allocations

If you want to run those services and the Pact broker in your local environment you will need following ports available 
in your environment.

|Application|Port|Protocol|
|--------|--------|------------|
|a-service|8083|HTTP/REST|
|b-service|8084|HTTP/REST|
|c-service|8085|HTTP/REST|
|c-service|8082|gRPC|
|d-service|8086|HTTP/REST/GraphQL|
|Pact Broker|80|HTTP/REST|

##Credits

* Enabling gRPC contract tests within Pact is made possible using the information provided in this 
  [page](https://medium.com/@ivangsa/consumer-driven-contract-testing-for-grpc-pact-io-d60155d21c4c), so thanks
  to **Ivan Garcia Sainz-Aja** for his invaluable contribution.
  
* General information shared in these three blog post series 
  [1](https://blog.codecentric.de/en/2019/10/consumer-driven-contract-testing-with-pact/) ,
  [2](https://blog.codecentric.de/en/2019/11/message-pact-contract-testing-in-event-driven-applications/) ,
  [3](https://blog.codecentric.de/en/2020/02/implementing-a-consumer-driven-contract-testing-workflow-with-pact-broker-and-gitlab-ci/) 
  published by **Frank Rosner** and **Raffael Stein** were very useful in organizing my thoughts about what kind of 
  communication methods to test. I also followed their proposed approach to deal with handling contract tests of async 
  request/commands, so thanks to their invaluable contributions as well.