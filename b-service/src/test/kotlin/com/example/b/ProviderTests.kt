package com.example.b

import au.com.dius.pact.provider.junit.Provider
import au.com.dius.pact.provider.junit.State
import au.com.dius.pact.provider.junit.VerificationReports
import au.com.dius.pact.provider.junit.loader.PactBroker
import au.com.dius.pact.provider.junit5.HttpTestTarget
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort

@Provider("b-service")
// @PactFolder("pacts")
@PactBroker(host = "localhost", scheme = "http", port = "80")
@VerificationReports
// @ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProviderTests {

    @LocalServerPort
    protected var port: Int = 0

    @MockBean
    private lateinit var currencyService: CurrencyService

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

    @State("list_of_available_currencies_when_user_has_existing_currencies")
    fun returnCurrencies(): List<Currency> {
        return Mockito.doReturn(listOf(Currency("USD"), Currency("TRY"))).`when`(currencyService).getCurrencies("foo")
    }

    @State("list_of_available_currencies_when_user_has_no_currencies")
    fun returnCurrenciesWhenNoneExists(): List<Currency> {
        return Mockito.doReturn(listOf<Currency>()).`when`(currencyService).getCurrencies("bar")
    }
}
