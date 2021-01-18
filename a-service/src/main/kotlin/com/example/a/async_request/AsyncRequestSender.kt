package com.example.a.async_request

import com.google.protobuf.Message
import org.springframework.stereotype.Component

@Component
class AsyncRequestSender {
    fun send(request: Message) {
        print("Async request sent :$request")
    }
}
