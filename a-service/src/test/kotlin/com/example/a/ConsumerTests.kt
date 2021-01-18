package com.example.a

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.RequestResponsePact
import au.com.dius.pact.core.model.annotations.Pact
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.Currency

@ExtendWith(PactConsumerTestExt::class)
@PactTestFor(providerName = "b-service")
class ConsumerTests {

    @Pact(consumer = "a-service")
    fun contractForGetCurrenciesWhenCurrenciesExist(builder: PactDslWithProvider): RequestResponsePact {
        return builder
            .given("list_of_available_currencies_when_user_has_existing_currencies")
            .uponReceiving("list of available currencies when user has existing currencies")
            .path("/rest/currencies/foo")
            .method("GET")
            .willRespondWith()
            .status(200)
            .headers(mapOf(Pair("Content-Type", "application/json")))
            .body(
                """
                [
                    {
                        "currencyCode":"USD"
                    },
                    {
                        "currencyCode":"TRY"
                    }
                ]
                """.trimIndent()
            )
            .toPact()
    }

    @Pact(consumer = "a-service")
    fun contractForGetCurrenciesWhenNoCurrencyExists(builder: PactDslWithProvider): RequestResponsePact {
        return builder
            .given("list_of_available_currencies_when_user_has_no_currencies")
            .uponReceiving("list of available currencies when none exists")
            .path("/rest/currencies/bar")
            .method("GET")
            .willRespondWith()
            .status(200)
            .headers(mapOf(Pair("Content-Type", "application/json")))
            .body(
                """
                [
                ]
                """.trimIndent()
            )
            .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "contractForGetCurrenciesWhenCurrenciesExist")
    fun testGetCurrenciesWithConsumerSpecificAPI(mockServer: MockServer) {
        val currencyService = CurrencyService()
        currencyService.baseUrl = mockServer.getUrl()
        val actualCurrencies = currencyService.getCurrencies("foo")
        MatcherAssert.assertThat(
            actualCurrencies,
            Matchers.containsInAnyOrder(Currency.getInstance("USD"), Currency.getInstance("TRY"))
        )
    }

    @Test
    @PactTestFor(pactMethod = "contractForGetCurrenciesWhenNoCurrencyExists")
    fun testGetCurrenciesWhenNoneExistsWithConsumerSpecificAPI(mockServer: MockServer) {
        val currencyAPI = CurrencyService()
        currencyAPI.baseUrl = mockServer.getUrl()
        val actualCurrencies = currencyAPI.getCurrencies("bar")
        MatcherAssert.assertThat(
            actualCurrencies.isEmpty(),
            Matchers.`is`(true)
        )
    }

    /*
        @Test
        @PactTestFor(pactMethod = "contractForGetCurrenciesWhenCurrenciesExist")
        fun testGetCurrencies(mockServer: MockServer) {
            val restTemplate = RestTemplate()
            val responseEntity = restTemplate.getForEntity(mockServer.getUrl() + "/rest/currencies", String::class.java)

            MatcherAssert.assertThat(responseEntity.statusCode.value(), Matchers.equalTo(200))
            JSONAssert.assertEquals(
                """
                    [
                        {
                            "currencyCode":"USD"
                        },
                        {
                            "currencyCode":"TRY"
                        }
                    ]
                """.trimIndent(), responseEntity.body, false
            )
        }

        @Test
        @PactTestFor(pactMethod = "contractForGetCurrenciesWhenNoCurrencyExists")
        fun testGetCurrenciesWhenNoneExists(mockServer: MockServer) {
            val restTemplate = RestTemplate()
            val responseEntity = restTemplate.getForEntity(mockServer.getUrl() + "/rest/currencies", String::class.java)

            MatcherAssert.assertThat(responseEntity.statusCode.value(), Matchers.equalTo(200))
            JSONAssert.assertEquals(
                """
                    [
                    ]
                """.trimIndent(), responseEntity.body, false
            )
        }
     */
}
