package com.example.a

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.ApplicationPidFileWriter

@SpringBootApplication
class AServiceApplication

fun main(args: Array<String>) {
    // runApplication<AServiceApplication>(*args)
    val app = SpringApplication(AServiceApplication::class.java)
    app.addListeners(ApplicationPidFileWriter("/tmp/a-service.pid"))
    app.run(*args)
}
