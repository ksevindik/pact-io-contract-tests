package com.example.b

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.ApplicationPidFileWriter
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@SpringBootApplication
class BServiceApplication

fun main(args: Array<String>) {
    // runApplication<BServiceApplication>(*args)
    val app = SpringApplication(BServiceApplication::class.java)
    app.addListeners(ApplicationPidFileWriter("/tmp/b-service.pid"))
    app.run(*args)
}

@Controller
class IndexController {
    @RequestMapping("/")
    fun index(): String {
        return "redirect:/swagger-ui.html"
    }
}
