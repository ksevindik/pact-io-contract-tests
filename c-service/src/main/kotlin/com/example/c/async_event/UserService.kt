package com.example.c.async_event

import com.example.event.UserCreatedEvent
import org.springframework.stereotype.Service

@Service
class UserService {
    fun userCreated(userCreatedEvent: UserCreatedEvent) {
        print("Received user created :$userCreatedEvent")
    }
}
