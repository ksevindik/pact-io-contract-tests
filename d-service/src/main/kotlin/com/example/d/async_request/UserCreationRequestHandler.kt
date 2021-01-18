package com.example.d.async_request

import com.example.d.async_event.User
import com.example.d.async_event.UserService
import com.example.msg.UserCreationRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserCreationRequestHandler @Autowired constructor(private val userService: UserService) {
    fun handle(userCreationRequest: UserCreationRequest) {
        val user = User(userCreationRequest.firstName, userCreationRequest.lastName, userCreationRequest.email)
        userService.create(user)
    }
}
