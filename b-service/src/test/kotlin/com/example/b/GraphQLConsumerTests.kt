package com.example.b

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.RequestResponsePact
import au.com.dius.pact.core.model.annotations.Pact
import com.example.b.graphql.User
import com.example.b.graphql.UserService
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(PactConsumerTestExt::class)
@PactTestFor(providerName = "d-service")
class GraphQLConsumerTests {
    @Pact(consumer = "b-service")
    fun contractForGetUsersWithCountry(builder: PactDslWithProvider): RequestResponsePact {
        return builder
                .given("get_users_with_country")
                .uponReceiving("get users with their countries")
                .path("/graphql")
                .method("POST")
                .body("""
                    {
                    "operationName":"getUsers",
                    "variables":{},
                    "query":"query getUsers { getUsers { __typename firstName lastName email country { __typename name } } }"
                    }
                """.trimIndent())
                .willRespondWith()
                .status(200)
                .headers(mapOf(Pair("Content-Type", "application/json")))
                .body("""
                    {
                      "data": {
                        "getUsers": [
                          {
                            "__typename":"User",
                            "firstName": "a",
                            "lastName": "b",
                            "email": "a@b",
                            "country": {
                              "__typename":"Country",
                              "name": "c1"
                            }
                          }
                        ]
                      }
                    }
                """.trimIndent())
                .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "contractForGetUsersWithCountry")
    fun testGetUsers(mockServer: MockServer) {
        val userService = UserService()
        userService.graphqlServerUrl = mockServer.getUrl()
        val users = userService.getUsers()
        MatcherAssert.assertThat(users,Matchers.containsInAnyOrder(User("a","b","a@b","c1")))
    }
}