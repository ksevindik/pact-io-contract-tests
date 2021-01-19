package com.example.d.async

import com.google.protobuf.Message
import org.springframework.stereotype.Component

@Component
class EventPublisher {
    fun publish(event: Message) {
        print("Event published :$event")
    }
}
