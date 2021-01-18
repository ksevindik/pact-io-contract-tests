package com.example.d

import com.example.msg.UserCreationRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserCreationRequestConsumer @Autowired constructor(private val userService: UserService) {
    fun handle(userCreationRequest: UserCreationRequest) {
        val user = User(userCreationRequest.firstName, userCreationRequest.lastName, userCreationRequest.email)
        userService.create(user)
    }
}
