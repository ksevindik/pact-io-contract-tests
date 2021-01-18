package com.example.b

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.RequestResponsePact
import au.com.dius.pact.core.model.annotations.Pact
import com.example.hello.HelloRequest
import io.grpc.Channel
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(PactConsumerTestExt::class)
@PactTestFor(providerName = "c-service")
class ConsumerTests {
    @Pact(consumer = "b-service")
    fun contractForSayHello(builder: PactDslWithProvider): RequestResponsePact {
        return builder
                .given("hello_world")
                .uponReceiving("hello world")
                .path("/grpc/helloworld.Greeter/SayHello")
                .method("POST")
                .body("""
                    {
                        "name":"kenan"
                    }
                """.trimIndent())
                .willRespondWith()
                .status(200)
                .headers(mapOf(Pair("Content-Type", "application/json")))
                .body("""
                    {
                        "message":"Hello from gRPC :kenan"
                    }
                """.trimIndent())
                .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "contractForSayHello")
    fun testSayHello(mockServer: MockServer) {
        val helloServiceClientAPI = HelloServiceClientAPIGrpcImpl(object : ChannelFactory {
            override fun createChannel(): Channel {
                return GrpcHttpChannel("localhost", mockServer.getPort())
            }
        })

        val request = HelloRequest.newBuilder().setName("kenan").build()

        val response = helloServiceClientAPI.sayHello(request)

        var greeting = response.message

        MatcherAssert.assertThat(
                greeting,
                Matchers.equalTo("Hello from gRPC :kenan"))
    }
}
