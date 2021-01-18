package com.example.c

import au.com.dius.pact.consumer.MessagePactBuilder
import au.com.dius.pact.consumer.dsl.PactDslJsonBody
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.consumer.junit5.ProviderType
import au.com.dius.pact.core.model.annotations.Pact
import au.com.dius.pact.core.model.messaging.Message
import au.com.dius.pact.core.model.messaging.MessagePact
import com.example.c.async_event.UserCreatedEventConsumer
import com.example.c.async_event.UserService
import com.example.event.UserCreatedEvent
import com.google.protobuf.util.JsonFormat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito

@ExtendWith(PactConsumerTestExt::class)
@PactTestFor(providerName = "d-service", providerType = ProviderType.ASYNCH)
class AsyncEventConsumerTests {
    @Pact(consumer = "c-service")
    fun contractForUserCreatedEvent(builder: MessagePactBuilder): MessagePact {
        return builder
                .hasPactWith("d-service")
                .expectsToReceive("user created event")
                .withContent(PactDslJsonBody()
                        .stringValue("firstName", "Foo")
                        .stringValue("lastName", "Bar")
                        .stringValue("email", "a@b"))
                .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "contractForUserCreatedEvent")
    fun testUserCreatedEvent(messages: List<Message>) {
        messages.forEach({
            val parser = JsonFormat.parser()
            val builder = UserCreatedEvent.newBuilder()
            parser.merge(it.contentsAsString(), builder)
            val event = builder.build()

            val userService = Mockito.mock(UserService::class.java)
            val eventHandler = UserCreatedEventConsumer(userService)
            eventHandler.handle(event)

            Mockito.verify(userService).userCreated(
                    UserCreatedEvent.newBuilder()
                            .setFirstName("Foo")
                            .setLastName("Bar")
                            .setEmail("a@b")
                            .build())
        })
    }
}
