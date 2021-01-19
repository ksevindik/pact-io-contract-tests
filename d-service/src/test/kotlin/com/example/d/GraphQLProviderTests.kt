package com.example.d

import au.com.dius.pact.provider.junit.Provider
import au.com.dius.pact.provider.junit.State
import au.com.dius.pact.provider.junit.VerificationReports
import au.com.dius.pact.provider.junit.loader.PactBroker
import au.com.dius.pact.provider.junit5.HttpTestTarget
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import com.example.d.model.Country
import com.example.d.model.User
import com.example.d.service.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.TestPropertySource

@Provider("d-service")
// @PactFolder("pacts")
@PactBroker(host = "localhost", scheme = "http", port = "80",consumers = ["b-service"])
@VerificationReports
// @ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GraphQLProviderTests {

    @LocalServerPort
    protected var port: Int = 0

    @MockBean
    private lateinit var userService: UserService

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

    @State("get_users_with_country")
    fun returnUsers(): List<User> {
        val user = User("a","b","a@b")
        user.id = 1L
        val country = Country("c1")
        country.id = 1L
        user.country = country

        return Mockito.doReturn(listOf(user)).`when`(userService).getUsers()
    }
}
