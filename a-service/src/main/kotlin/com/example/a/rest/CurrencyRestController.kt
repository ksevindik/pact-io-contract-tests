package com.example.a.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class CurrencyRestController @Autowired constructor(private val currencyService: CurrencyService) {
    @GetMapping("/{username}")
    fun getCurrencies(@PathVariable("username") username: String): List<Currency> {
        return currencyService.getCurrencies(username)
    }
}
