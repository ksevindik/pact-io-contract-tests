package com.example.a

import au.com.dius.pact.consumer.MessagePactBuilder
import au.com.dius.pact.consumer.dsl.PactDslJsonBody
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.consumer.junit5.ProviderType
import au.com.dius.pact.core.model.annotations.Pact
import au.com.dius.pact.core.model.messaging.Message
import au.com.dius.pact.core.model.messaging.MessagePact
import com.example.msg.UserCreationRequest
import com.google.protobuf.util.JsonFormat
import com.nhaarman.mockitokotlin2.argumentCaptor
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito

@ExtendWith(PactConsumerTestExt::class)
@PactTestFor(providerName = "d-service", providerType = ProviderType.ASYNCH)
class AsyncRequestConsumerTests {
    @Pact(consumer = "a-service")
    fun contractForUserCreationRequest(builder: MessagePactBuilder): MessagePact {
        return builder
                .hasPactWith("d-service")
                .expectsToReceive("user creation request")
                .withContent(PactDslJsonBody()
                        .stringValue("firstName", "Foo")
                        .stringValue("lastName", "Bar")
                        .stringValue("email", "a@b"))
                .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "contractForUserCreationRequest")
    fun testUserCreationRequest(messages: List<Message>) {
        messages.forEach({
            val parser = JsonFormat.parser()
            val builder = UserCreationRequest.newBuilder()
            parser.merge(it.contentsAsString(), builder)
            val expectedAsyncRequest = builder.build()

            val asyncRequestSender = Mockito.mock(AsyncRequestSender::class.java)
            val userService = UserService(asyncRequestSender)

            userService.create(User("Foo", "Bar", "a@b"))

            val argCaptor = argumentCaptor<UserCreationRequest>()
            Mockito.verify(asyncRequestSender).send(argCaptor.capture())
            val actualAsyncRequest = argCaptor.firstValue
            MatcherAssert.assertThat(actualAsyncRequest, Matchers.equalTo(expectedAsyncRequest))
        })
    }
}
