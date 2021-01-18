package com.example.a

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.ApplicationPidFileWriter
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@SpringBootApplication
class AServiceApplication

fun main(args: Array<String>) {
    // runApplication<AServiceApplication>(*args)
    val app = SpringApplication(AServiceApplication::class.java)
    app.addListeners(ApplicationPidFileWriter("/tmp/a-service.pid"))
    app.run(*args)
}

@Controller
class IndexController {
    @RequestMapping("/")
    fun index(): String {
        return "redirect:/swagger-ui.html"
    }
}
