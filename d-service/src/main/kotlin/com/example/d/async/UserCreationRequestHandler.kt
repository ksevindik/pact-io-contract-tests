package com.example.d.async

import com.example.d.model.User
import com.example.d.service.UserService
import com.example.msg.UserCreationRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserCreationRequestHandler @Autowired constructor(private val userService: UserService) {
    fun handle(userCreationRequest: UserCreationRequest) {
        val user = User(userCreationRequest.firstName, userCreationRequest.lastName, userCreationRequest.email)
        userService.create(user)
    }
}
