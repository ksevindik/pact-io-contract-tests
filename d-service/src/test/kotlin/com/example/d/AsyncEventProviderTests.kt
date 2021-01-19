package com.example.d

import au.com.dius.pact.provider.PactVerifyProvider
import au.com.dius.pact.provider.junit.Provider
import au.com.dius.pact.provider.junit.VerificationReports
import au.com.dius.pact.provider.junit.loader.PactBroker
import au.com.dius.pact.provider.junit5.AmpqTestTarget
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import com.example.d.async.EventPublisher
import com.example.d.model.User
import com.example.d.service.UserService
import com.example.event.UserCreatedEvent
import com.google.protobuf.util.JsonFormat
import com.nhaarman.mockitokotlin2.argumentCaptor
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.TestPropertySource

@Provider("d-service")
// @PactFolder("pacts")
@PactBroker(host = "localhost", scheme = "http", port = "80",consumers = ["c-service"])
@VerificationReports
// @ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = ["grpc.server.port=8081"])
class AsyncEventProviderTests {

    @LocalServerPort
    protected var port: Int = 0

    @MockBean
    private lateinit var eventPublisher: EventPublisher

    @Autowired
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp(context: PactVerificationContext) {
        context.target = AmpqTestTarget()
        System.setProperty("pact.verifier.publishResults", "true")
        System.setProperty("pact.provider.version", "0.0.1-SNAPSHOT")
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider::class)
    fun testTemplate(context: PactVerificationContext) {
        context.verifyInteraction()
    }

    @PactVerifyProvider("user created event")
    fun publishUserCreatedEvent(): String {
        val argCaptor = argumentCaptor<UserCreatedEvent>()
        val user = User("Foo", "Bar", "a@b")
        userService.create(user)
        Mockito.verify(eventPublisher).publish(argCaptor.capture())
        val event = argCaptor.firstValue
        val printer = JsonFormat.printer()
        val json = printer.print(event)
        return json
    }
}
