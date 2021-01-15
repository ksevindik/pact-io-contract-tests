package com.example.a

import java.util.Currency

interface CurrencyServiceClientAPI {
    fun getCurrencies(username: String): List<Currency>
}
