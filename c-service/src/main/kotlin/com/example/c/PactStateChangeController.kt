package com.example.c

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/pact")
class PactStateChangeController {
    @PostMapping("/stateChange")
    fun handle(@RequestBody map: Map<String, String>) {
        println(">>>$map")
    }
}
