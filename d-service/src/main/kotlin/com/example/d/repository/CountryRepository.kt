package com.example.d.repository

import com.example.d.model.Country
import org.springframework.stereotype.Repository

@Repository
class CountryRepository {
    fun findById(id:Long):Country? {
        when(id) {
            1L -> {
                val c1 = Country("c1")
                c1.id = id
                return c1
                }
            2L -> {
                val c2 = Country("c2")
                c2.id = id
                return c2
            }
            else -> return null
        }
    }
}