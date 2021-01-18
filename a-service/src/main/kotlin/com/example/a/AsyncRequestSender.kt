package com.example.a

import com.google.protobuf.Message
import org.springframework.stereotype.Component

@Component
class AsyncRequestSender {
    fun send(request: Message) {
        print("Async request sent :$request")
    }
}
