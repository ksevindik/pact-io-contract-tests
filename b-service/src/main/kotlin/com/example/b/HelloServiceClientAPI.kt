package com.example.b

import com.example.hello.HelloReply
import com.example.hello.HelloRequest

interface HelloServiceClientAPI {
    fun sayHello(helloRequest: HelloRequest): HelloReply
}
