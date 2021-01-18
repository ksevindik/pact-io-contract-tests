package com.example.c

import com.example.event.UserCreatedEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserCreatedEventConsumer @Autowired constructor(private val userService: UserService) {
    fun handle(userCreatedEvent: UserCreatedEvent) {
        userService.userCreated(userCreatedEvent)
    }
}
