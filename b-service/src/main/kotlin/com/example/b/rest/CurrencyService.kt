package com.example.b.rest

import org.springframework.stereotype.Service

@Service
class CurrencyService {
    fun getCurrencies(username: String): List<Currency> {
        return listOf(Currency("EUR"))
    }
}
