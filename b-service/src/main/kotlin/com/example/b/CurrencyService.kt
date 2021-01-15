package com.example.b

import org.springframework.stereotype.Service

@Service
class CurrencyService {
    fun getCurrencies(username: String): List<Currency> {
        return listOf(Currency("EUR"))
    }
}
