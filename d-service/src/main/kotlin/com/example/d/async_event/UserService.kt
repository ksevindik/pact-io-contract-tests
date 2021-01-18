package com.example.d.async_event

import com.example.event.UserCreatedEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(private val eventPublisher: EventPublisher) {
    fun create(user: User) {
        print("User created :$user")
        eventPublisher.publish(
                UserCreatedEvent.newBuilder()
                        .setFirstName(user.firstName)
                        .setLastName(user.lastName)
                        .setEmail(user.email).build())
    }
}
