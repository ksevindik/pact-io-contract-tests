package com.example.b

import com.example.hello.HelloRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloWorldController @Autowired constructor(private val helloService: HelloService) {

    @GetMapping("/hello")
    fun hello(@RequestParam(name = "name", defaultValue = "foo") name: String): String {
        val request = HelloRequest.newBuilder().setName(name).build()

        val response = helloService.sayHello(request)

        var greeting = response.message

        return greeting
    }
}
