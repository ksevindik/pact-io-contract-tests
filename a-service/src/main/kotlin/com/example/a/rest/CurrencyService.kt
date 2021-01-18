package com.example.a.rest

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.*
import kotlin.collections.HashMap

@Component
class CurrencyService() {

    var restTemplate = RestTemplate()

    var baseUrl: String = "http://localhost:8084/"

    fun getCurrencies(username: String): List<Currency> {
        val responseEntity = restTemplate.getForEntity("$baseUrl/rest/currencies/$username", String::class.java)
        if (responseEntity.statusCodeValue == 200) {
            val mapper = ObjectMapper()
            return mapper.readValue(responseEntity.body, object : TypeReference<List<HashMap<String, String>>>() {})
                .map { it.get("currencyCode") }.map { Currency.getInstance(it as String) }
        } else {
            throw RuntimeException("Remote currency service invocation failed with code ${responseEntity.statusCodeValue}")
        }
    }
}
