package com.example.d.graphql

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.example.d.model.User
import com.example.d.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserRootResolver @Autowired constructor(private val userService: UserService)
    : GraphQLQueryResolver, GraphQLMutationResolver  {
    fun getUsers():List<UserGQLType> {
        return userService.getUsers().map { UserGQLType(it.id!!,it.country!!.id!!,it.firstName,it.lastName,it.email) }
    }

    fun createUser(firstName:String,lastName:String,email:String,countryId:Long) : UserGQLType {
        val user = User(firstName,lastName,email)
        userService.create(user)
        return UserGQLType(user.id!!,countryId,firstName,lastName,email)
    }
}

data class UserGQLType(val id:Long,val countryId:Long, val firstName:String,val lastName:String,val email:String)

data class CountryGQLType(val id:Long, val name:String)