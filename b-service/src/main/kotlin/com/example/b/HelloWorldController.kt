package com.example.b

import com.example.hello.GreeterGrpc
import com.example.hello.HelloRequest
import io.grpc.ManagedChannelBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloWorldController() {

    @Value("\${grpc.server.port}")
    var grpcServerPort: Int = 8082

    @GetMapping("/hello")
    fun hello(@RequestParam(name = "name", defaultValue = "foo") name: String): String {
        var mc = ManagedChannelBuilder.forAddress("localhost", grpcServerPort).usePlaintext().build()

        var stub = GreeterGrpc.newBlockingStub(mc)

        var response = stub.sayHello(HelloRequest.newBuilder().setName(name).build())

        var greeting = response.message

        return greeting
    }
}
