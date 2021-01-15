package com.example.b

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.ApplicationPidFileWriter

@SpringBootApplication
class BServiceApplication

fun main(args: Array<String>) {
    // runApplication<BServiceApplication>(*args)
    val app = SpringApplication(BServiceApplication::class.java)
    app.addListeners(ApplicationPidFileWriter("/tmp/b-service.pid"))
    app.run(*args)
}
