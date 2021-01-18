package com.example.c

import au.com.dius.pact.provider.junit.Provider
import au.com.dius.pact.provider.junit.State
import au.com.dius.pact.provider.junit.VerificationReports
import au.com.dius.pact.provider.junit.loader.PactBroker
import au.com.dius.pact.provider.junit5.HttpTestTarget
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import com.example.c.grpc.HelloService
import com.example.hello.HelloReply
import com.example.hello.HelloRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.TestPropertySource

@Provider("c-service")
// @PactFolder("pacts")
@PactBroker(host = "localhost", scheme = "http", port = "80")
@VerificationReports
// @ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = ["grpc.server.port=8081"])
class GrpcProviderTests {

    @LocalServerPort
    protected var port: Int = 0

    @MockBean
    private lateinit var helloService: HelloService

    @BeforeEach
    fun setUp(context: PactVerificationContext) {
        context.target = HttpTestTarget(port = port)
        System.setProperty("pact.verifier.publishResults", "true")
        System.setProperty("pact.provider.version", "0.0.1-SNAPSHOT")
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider::class)
    fun testTemplate(context: PactVerificationContext) {
        context.verifyInteraction()
    }

    @State("hello_world")
    fun sayHello(): HelloReply {
        val req = HelloRequest.newBuilder().setName("kenan").build()
        val resp = HelloReply.newBuilder().setMessage("Hello from gRPC :kenan").build()
        return Mockito.doReturn(resp).`when`(helloService).sayHello(req)
    }
}
