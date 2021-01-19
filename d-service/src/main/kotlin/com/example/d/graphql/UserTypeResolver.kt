package com.example.d.graphql

import com.coxautodev.graphql.tools.GraphQLResolver
import com.example.d.repository.CountryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserTypeResolver @Autowired constructor(private val countryRepository:CountryRepository)
    : GraphQLResolver<UserGQLType> {

    fun getCountry(user:UserGQLType) : CountryGQLType {
        val c = countryRepository.findById(user.countryId)
        if(c == null) throw IllegalStateException("No country found with id ${user.countryId}")
        return CountryGQLType(c.id!!,c.name)
    }
}