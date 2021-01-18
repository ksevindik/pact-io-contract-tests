package com.example.d

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@SpringBootApplication
class DServiceApplication

fun main(args: Array<String>) {
    runApplication<DServiceApplication>(*args)
}

@Controller
class IndexController {
    @RequestMapping("/")
    fun index(): String {
        return "redirect:/swagger-ui.html"
    }
}
