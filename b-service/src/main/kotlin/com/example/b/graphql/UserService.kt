package com.example.b.graphql

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Error
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class UserService {
    var graphqlServerUrl = "http://localhost:8086/graphql"

    fun getUsers() : List<User> {
        val apolloClient = ApolloClient.builder().serverUrl(graphqlServerUrl).build()
        var users : List<GetUsersQuery.GetUser?>? = null
        var errors : List<Error>? = null
        runBlocking {
            val response = try {
                apolloClient.query(GetUsersQuery()).toDeferred().await()
            } catch (ex:ApolloException) {
                return@runBlocking
            }

            if(response.hasErrors()) {
                errors = response.errors
            } else {
                users = response.data!!.getUsers
            }
        }
        if(errors != null) {
            throw RuntimeException("Error occurred while executing graphql query $errors")
        }
        return users!!.map { User(it!!.firstName,it.lastName,it.email,it.country.name) }
    }
}