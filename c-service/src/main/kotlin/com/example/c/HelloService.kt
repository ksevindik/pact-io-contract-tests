package com.example.c

import com.example.hello.HelloReply
import com.example.hello.HelloRequest
import org.springframework.stereotype.Service

@Service
class HelloService {
    fun sayHello(request: HelloRequest): HelloReply {
        val reply = HelloReply.newBuilder().setMessage("Hello from gRPC :" + request.name).build()
        return reply
    }
}
